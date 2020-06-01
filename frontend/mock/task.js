const task = require('./db').task
const taskSubmission = require('./db').taskSubmission
const taskCorrection = require('./db').taskCorrection
const user = require('./db').user
const uuid = require('uuid').v4

export default [
  // get tasks
  {
    url: '/vue-element-admin/task/get_tasks',
    type: 'get',
    response: config => {
      const { courseId, userId } = config.query

      if(!courseId || !userId){
        return {
          code: 40000,
          msg: 'course id cannot be null'
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
      if(role === 'student'){
        const tasks = []
        for(let t of task){
          if(t.courseId === courseId){
            const newT = Object.assign({}, t)
            let submission = null
            for(let ts of taskSubmission){
              if(ts.taskId === t.taskId && ts.userId === userId){
                submission = ts
              }
            }
            newT['submissionState'] = submission ? '0' : submission['state']
            tasks.push(newT)
          }
        }

        return {
          code: 20000,
          data: tasks
        }
      }else if(role === 'teacher'){
        const tasks = []
        for(let t of task){
          if(t.courseId === courseId){
            tasks.push(t)
          }
        }

        return {
          code: 20000,
          data: tasks
        }
      }else{
        return {
          code: 50000,
          msg: 'invalid record saved into db'
        }
      }
    }
  },

  // get one of the task
  {
    url: '/vue-element-admin/task/get_task',
    type: 'get',
    response: config => {
      const { taskId } = config.query

      if(!taskId){
        return {
          code: 40000,
          msg: 'task id cannot be null'
        }
      }

      let ret = {}
      for(let t of task){
        if(t.taskId === taskId){
          ret = t
          break
        }
      }

      return {
        code: 20000,
        data: ret
      }
    }
  },

  {
    url: '/vue-element-admin/task/get_submissions',
    type: 'get',
    response: config => {
      const { taskId } = config.query
      if(!taskId){
        return {
          code: 40000,
          msg: 'task id cannot be null'
        }
      }

      let ret = []
      for(let t of taskSubmission){
        const tsb = Object.assign({}, t)
        if(t.taskId === taskId){
          for(let u of user){
            if(u.userId === t.userId){
              tsb['name'] = u.name
              ret.push(tsb)
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

  {
    url: '/vue-element-admin/task/get_submission',
    type: 'get',
    response: config => {
      const { taskId, userId } = config.query
      if(!taskId || !userId){
        return {
          code: 40000,
          msg: 'task id or user id cannot be null'
        }
      }

      let index = -1
      for(let i = 0; i < taskSubmission.length; ++i){
        if(taskSubmission[i].userId === userId && taskSubmission[i].taskId === taskId){
          index = i
        }
      }

      if(index === -1){
        return {
          code: 40004,
          msg: 'find no submission corresponding to user id and task id'
        }
      }
      return {
        code: 20000,
        data: taskSubmission[index]
      }
    }
  },

  // create task
  {
    url: '/vue-element-admin/task/create_task',
    type: 'post',
    response: config => {
      const { courseId, title, state, deadline } = config.body

      if(!courseId || !title || !deadline || !(state === '0' || state === '1')){
        return {
          code: 40000,
          msg: 'course id, title, deadline cannot be null or state is not \'0\' or \'1\''
        }
      }

      const newT = Object.assign({}, config.body, {taskId: uuid(), createdAt: new Date().toUTCString(), updatedAt: new Date().toUTCString()})
      task.push(newT)
      return {
        code: 20000,
        data: newT
      }
    }
  },

  // update task
  {
    url: '/vue-element-admin/task/update_task',
    type: 'post',
    response: config => {
      const { taskId } = config.body

      if(!taskId){
        return {
          code: 40000,
          msg: 'task id cannot be null'
        }
      }

      let index = -1
      for(let i = 0; i < task.length; ++i){
        if(task[i].taskId === taskId){
          index = i
          break
        }
      }
      if(index === -1){
        return {
          code: 40004,
          msg: 'no task corresponding to the task id'
        }
      }

      const newT = Object.assign({}, config.body, {courseId: task[index].courseId})
      task.splice(index, 1)
      task.push(newT)
      return {
        code: 20000,
        data: newT
      }
    }
  },

  // delete task
  {
    url: '/vue-element-admin/task/delete_task',
    type: 'post',
    response: config => {
      const { taskId } = config.body

      if(!taskId){
        return {
          code: 40000,
          msg: 'task id cannot be null'
        }
      }

      let index = -1
      for(let i = 0; i < task.length; ++i){
        if(task[i].taskId === taskId){
          index = i
          break
        }
      }

      if(index === -1){
        return {
          code: 40004,
          msg: 'no task corresponding to the task id and course id'
        }
      }
      task.splice(index, 1)
      return {
        code: 20000,
        data: {}
      }
    }
  },

  // submit task
  {
    url: '/vue-element-admin/task/submit_task',
    type: 'post',
    response: config => {
      const { taskId, userId, state } = config.body

      if(!taskId || !userId || !state){
        return {
          code: 40000,
          msg: 'task id or user id or state cannot be null'
        }
      }

      let index = -1
      for(let i = 0; i < taskSubmission.length; ++i){
        if(taskSubmission[i].taskId === taskId && taskSubmission[i].userId === userId){
          index = i
          break
        }
      }

      if(index >= 0){
        taskSubmission.splice(index, 1)
        taskSubmission.push(config.body)
        return {
          code: 20000,
          data: {}
        }
      }

      taskSubmission.push(config.body)
      return {
        code: 20000,
        data: {}
      }
    }
  },

  // correct task
  {
    url: '/vue-element-admin/task/correct_task',
    type: 'post',
    response: config => {
      const { taskId, userId, state } = config.body

      if(!taskId || !userId || !state){
        return {
          code: 40000,
          msg: 'task id or user id or state cannot be null'
        }
      }

      let index = -1
      for(let i = 0; i < taskCorrection.length; ++i){
        if(taskCorrection[i].taskId === taskId && taskCorrection[i].userId === userId){
          index = i
          break
        }
      }

      if(index >= 0){
        taskCorrection.splice(index, 1)
        taskCorrection.push(config.body)
        return {
          code: 20000,
          data: {}
        }
      }

      taskCorrection.push(config.body)
      return {
        code: 20000,
        data: {}
      }
    }
  },

  // get task analysis
  {
    url: '/vue-element-admin/task/get_task_analysis',
    type: 'get',
    response: config => {
      const { taskId } = config.query

      if(!taskId){
        return {
          code: 40000,
          msg: 'task id or course id cannot be null'
        }
      }

      // TODO
    }
  }
]
