const course = require('./db').course
const user = require('./db').user
const selectCourse = require('./db').selectCourse
const teachCourse = require('./db').teachCourse
const uuid = require('uuid').v4
const { filterUserInfo } = require('./util')

export default [
  {
    url: '/vue-element-admin/course/get_all_courses',
    type: 'get',
    response: config => {
      const { userId } = config.query

      if(!userId){
        return {
          code: 40000,
          msg: 'user id cannot be null'
        }
      }

      let index = -1
      for(let i = 0; i < user.length; ++i){
        if(user[i].userId === userId){
          index = i
        }
      }
      if(index === -1){
        return {
          code: 40004,
          msg: 'cannot find user corresponding to the user id'
        }
      }

      const ret = []
      for(let c of course){
        let duplicate = false
        for(let sc of selectCourse){
          if(sc.courseId === c.courseId && sc.userId === userId){
            duplicate = true
            break
          }
        }
        if(duplicate) continue

        const userIds = []
        const teachers = []
        for(let tc of teachCourse){
          if(tc.courseId === c.courseId){
            userIds.push(tc.userId)
          }
        }
        for(let t of userIds){
          for(let u of user){
            if(u.userId === t){
              teachers.push(filterUserInfo(u))
            }
          }
        }
        const newC = Object.assign({}, c, {teachers})
        console.log(newC)
        ret.push(newC)
      }
      return {
        code: 20000,
        data: ret
      }
    }
  },

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
              for(let tc of teachCourse){
                if(tc.courseId === c.courseId){
                  for(let u of user){
                    if(tc.userId === u.userId){
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
      if(!avatar) newC['avatar'] = 'https://st-gdx.dancf.com/gaodingx/344/design/20190701-173956-77f2.png?x-oss-process=image/resize,w_675/interlace,1,image/format,jpg'
      newC['createdAt'] = new Date()
      course.push(newC)

      for(let t of teachers){
        teachCourse.push({userId: t, courseId: newC['courseId'], createdAt: new Date().toUTCString()})
      }
      const teacherDetail = []
      for(let t of teachers){
        for(let u of user){
          if(u.userId === t.userId){
            teacherDetail.push(filterUserInfo(u))
          }
        }
      }
      // 如果teachers不包含creator本身，那么在服务端自动加入
      if(teachers.indexOf(creator) === -1){
        teachCourse.push({userId: creator, courseId: newC['courseId'], createdAt: new Date().toUTCString()})
        teachers.push(creator)
      }
      return {
        code: 20000,
        data: Object.assign({}, newC, { teachers: teacherDetail })
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
        for(let u of user){
          if(u.userId === userId){
            selectCourse.push({
              courseId: courseId,
              userId: userId,
              createdAt: new Date().toUTCString()
            })
            let cs = null
            for(let c of course){
              if(c.courseId === courseId){
                const teachers = []
                for(let tc of teachCourse){
                  if(tc.courseId === courseId){
                    for(let u of user){
                      if(u.userId === tc.userId){
                        teachers.push(filterUserInfo(u))
                      }
                    }
                  }
                }
                cs = Object.assign({}, c, { teachers })
                break
              }
            }
            if(cs === null){
              return {
                code: 50000,
                msg: 'server error!'
              }
            }
            return {
              code: 20000,
              data: cs
            }
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
      const { courseId, userId } = config.body
      if(!courseId || !userId){
        return {
          code: 40000,
          msg: 'course id and user id and role cannot be null'
        }
      }
      let index = -1
      for(let i = 0; i < user.length; ++i){
        if(user[i].userId === userId){
          index = i
          break
        }
      }
      if(index === -1){
        return {
          code: 40004,
          msg: 'cannot find user corresponding to user id'
        }
      }

      const role = user[index].role
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
