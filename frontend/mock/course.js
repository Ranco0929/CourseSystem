const course = require('./db').course
const user = require('./db').user
const selectCourse = require('./db').selectCourse
const teachCourse = require('./db').teachCourse
const uuid = require('uuid').v4

export default [
  {
    url: '/vue-element-admin/course/get_courses',
    type: 'get',
    response: config => {
      const { userId } = config.query

      if(!userId){
        return {
          code: 40000,
          msg: 'user id cannot be null'
        }
      }
      let userInfo = null
      for(let u of user){
        if(u.userId === userId){
          userInfo = u
        }
      }
      if(!userInfo){
        return {
          code: 40004,
          msg: 'no user corresponding to user id'
        }
      }

      const cs = userInfo['role'] === 'student' ? selectCourse : teachCourse
      const ret = []
      for(let sc of cs){
        if(sc.userId === userId){
          for(let c of course){
            if(c.courseId === sc.courseId){
              const users = []
              for(let sc1 of cs){
                if(sc1.courseId === c.courseId){
                  for(let u of user){
                    if(sc1.userId === u.userId){
                      users.push(u)
                    }
                  }
                }
              }
              const newC = Object.assign({}, c, {teachers: users})
              ret.push(newC)
            }
          }
        }
      }
      return {
        code: 20000,
        data: ret
      }
    }
  },
  // create course
  {
    url: '/vue-element-admin/course/create_course',
    type: 'post',
    response: config => {
      const { name, info, creator, avatar, teachers } = config.body
      if(!name || !creator){
        return {
          code: 40000,
          msg: 'name or creator id cannot be null'
        }
      }

      let valid = false
      for(let u of user){
        if(u.userId === creator){
          valid = true
        }
      }
      for(let uid of teachers){
        let find = false
        for(let u of user) {
          if (u.userId === uid) find = true
        }
        valid &= find
      }
      if(!valid){
        return {
          code: 40004,
          msg: 'cannot find record corresponding to creator id or teachers id'
        }
      }

      const newC = {}
      newC['courseId'] = uuid()
      newC['name'] = name
      newC['info'] = info
      newC['creator'] = creator
      newC['avatar'] = avatar
      newC['createdAt'] = new Date()
      course.push(newC)

      for(let t of teachers){
        selectCourse.push({userId: t, courseId: newC['courseId'], createdAt: new Date().toUTCString()})
      }
      // 如果teachers不包含creator本身，那么在服务端自动加入
      if(teachers.indexOf(creator) === -1){
        selectCourse.push({userId: creator, courseId: newC['courseId'], createdAt: new Date().toUTCString()})
        teachers.push(creator)
      }
      return {
        code: 20000,
        data: Object.assign({}, newC, {teachers})
      }
    }
  },

  // add course
  {
    url: '/vue-element-admin/course/add_course',
    type: 'post',
    response: config => {
      const { courseId, userId } = config.body
      let valid = false
      for(let c of course){
        if(c.courseId === courseId) valid = true
      }
      if(valid){
        let find = false
        for(let u of user){
          if(u.userId === userId) find = true
        }
        if(find){
          const newSc = {
            courseId,
            userId,
            createdAt: new Date().toUTCString()
          }
          selectCourse.push(newSc)
          return {
            code: 20000,
            data: newSc
          }
        }
      }

      return {
        code: 40004,
        msg: 'cannot find the course and user corresponding to the course id and user id'
      }
    }
  },

  // delete course
  {
    url: '/vue-element-admin/course/delete_course',
    type: 'post',
    response: config => {
      const { courseId, userId, role } = config.body
      if(!courseId || !userId || !role){
        return {
          code: 40000,
          msg: 'course id and user id and role cannot be null'
        }
      }
      // 当角色为teacher时
      if(role === 'teacher'){
        let index = -1
        for(let i = 0; i < course.length; ++i){
          if(course[i].courseId === courseId){
            index = i
            break
          }
        }
        if(index >= 0){
          const c = course[index]
          if(c.creator === userId){
            const newSc = []
            for(let sc of selectCourse){
              if(sc.courseId !== courseId)  newSc.push(sc)
            }
            const newTc = []
            for(let tc of teachCourse){
              if(tc.courseId !== courseId)  newTc.push(tc)
            }
            let length = selectCourse.length
            for(let i = 0; i < length; ++i){
              selectCourse.pop()
            }
            for(let n of newSc){
              selectCourse.push(n)
            }

            length = teachCourse.length
            for(let i = 0; i < length; ++i){
              teachCourse.pop()
            }
            for(let n of newTc){
              teachCourse.push(n)
            }
            course.splice(index, 1)
            return {
              code: 20000,
              data:{}
            }
          }
          return {
            code: 40001,
            msg: 'the action is not permitted'
          }
        }

        return {
          code: 40004,
          msg: 'cannot find course corresponding to course id'
        }
      }
      // 当角色为student时
      else if(role === 'student'){
        let index = -1
        for(let i = 0; i < selectCourse.length; ++i){
          if(selectCourse[i].courseId === courseId && selectCourse[i].userId === userId){
            index = i
            break
          }
        }
        if(index >= 0){
          selectCourse.splice(index, 1)
          return {
            code: 20000,
            data:{}
          }
        }
        return {
          code: 40004,
          msg: 'cannot find the course that user selected'
        }
      }

      return {
        code: 40000,
        msg: 'role can only be student or teacher'
      }
    }
  },

  // get one of courses
  {
    url: '/vue-element-admin/course/get_course',
    type: 'get',
    response: config => {
      const { courseId, userId } = config.query

      if(!courseId || !userId){
        return {
          code: 40000,
          msg: 'course id or user id cannot be null'
        }
      }
      let userInfo = null
      for(let u of user){
        if(u.userId === userId){
          userInfo = u
          break
        }
      }

      if(!userInfo){
        return {
          code: 40004,
          msg: 'no user corresponding to user id'
        }
      }

      let index = -1
      for(let i = 0; i < course.length; ++i){
        if(course[i].courseId === courseId){
          index = i
          break
        }
      }
      const cs = userInfo['role'] === 'student' ? selectCourse : teachCourse
      if(index >= 0){
        const users = []
        for(let sc1 of cs){
          if(sc1.courseId === courseId){
            for(let u of user){
              if(sc1.userId === u.userId){
                users.push(u)
              }
            }
          }
        }
        const newC = Object.assign({}, course[index], {teachers: users})
        return {
          code: 20000,
          data: newC
        }
      }

      return {
        code: 40004,
        msg: 'no course corresponding to the course id'
      }
    }
  },

  // get students corresponding to the course
  {
    url: '/vue-element-admin/course/get_students',
    type: 'get',
    response: config => {
      const { courseId } = config.query
      if(!courseId){
        return {
          code: 40000,
          msg: 'course id cannot be null'
        }
      }

      const stu = []
      for(let sc of selectCourse){
        if(sc.courseId === courseId){
          for(let u of user){
            if(u.userId === sc.userId){
              const ret = {}
              ret['userId'] = u.userId
              ret['name'] = u.name
              ret['info'] = u.info
              ret['email'] = u.email
              ret['createdAt'] = u.createdAt
              ret['updatedAt'] = u.updatedAt
              stu.push(ret)
            }
          }
        }
      }
      return {
        code: 20000,
        data: stu
      }
    }
  }
]
