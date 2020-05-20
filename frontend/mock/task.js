const task = require('./db').task
const taskSubmission = require('./db').taskSubmission
const taskCorrection = require('./db').taskCorrection
const uuid = require('uuid').v4

export default [
  // get tasks
  {
    url: '/vue-element-admin/task/get_tasks',
    type: 'get',
    response: config => {
      const { courseId} = config.query

      if(!courseId){
        return {
          code: 40000,
          msg: 'course id cannot be null'
        }
      }

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
    }
  },

  // get one of the task
  {
    url: '/vue-element-admin/task/get_task',
    type: 'get',
    response: config => {
      const { taskId, courseId } = config.query
      if(!taskId || !courseId){
        return {
          code: 40000,
          msg: 'task id or course id cannot be null'
        }
      }

      let ret = {}
      for(let t of task){
        if(t.courseId === courseId && t.taskId === taskId){
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
      const { taskId, courseId } = config.body

      if(!taskId || !courseId){
        return {
          code: 40000,
          msg: 'task id or course id cannot be null'
        }
      }

      let index = -1
      for(let i = 0; i < task.length; ++i){
        if(task[i].taskId === taskId && task[i].courseId === courseId){
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
      const newT = Object.assign({}, config.body)
      task.push(newT)
      return {
        code: 20000,
        data: newT
      }
    }
  },

  // delete task
  {
    url: '/vue-element-admin/task/update_task',
    type: 'post',
    response: config => {
      const { taskId, courseId } = config.body

      if(!taskId || !courseId){
        return {
          code: 40000,
          msg: 'task id or course id cannot be null'
        }
      }

      let index = -1
      for(let i = 0; i < task.length; ++i){
        if(task[i].taskId === taskId && task[i].courseId === courseId){
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
      const { taskId, courseId } = config.query

      if(!taskId || !courseId){
        return {
          code: 40000,
          msg: 'task id or course id cannot be null'
        }
      }

      // TODO
    }
  }
]
