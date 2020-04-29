const taskCorrection = require('./db').taskCorrection

export default [
  // find
  {
    url: '/vue-element-admin/task-correction/find',
    type: 'post',
    response: config => {
      const { data } = config.body
      const limit = data.limit
      const sort = data.sort
      const keys = []

      for(let key in data){
        if(key !== 'sort' && key !== 'limit'){
          keys.push(key)
        }
      }

      const compare = function (origin, target){
        if (typeof target !== "object") {
          //target不是对象/数组
          return origin === target; //直接返回全等的比较结果
        }

        if (typeof origin !== "object") {
          //origin不是对象/数组
          return false; //直接返回false
        }
        for (let key of Object.keys(target)) {
          //遍历target的所有自身属性的key
          if (!compare(origin[key], target[key])) {
            //递归比较key对应的value，
            //value不等，则两对象不等，结束循环，退出函数，返回false
            return false;
          }
        }
        //遍历结束，所有value都深度比较相等，则两对象相等
        return true;
      }

      const isRange = function (o){
        return o['gt'] || o['gte'] || o['lt'] || o['lte']
      }

      let res = taskCorrection
      for(let key of keys){
        let temp = []
        if(typeof data[key] === 'object' && isRange(data[key])){
          let bounds = []
          for(let bound in data[key]){
            bounds.push(bound)
          }
          for(let rec in res){
            let valid = true
            if(data[key]['gt'] && rec[key] <= data[key]['gt'])  valid = false
            if(data[key]['gte'] && rec[key] < data[key]['gte']) valid = false
            if(data[key]['lt'] && rec[key] >= data[key]['lt'])  valid = false
            if(data[key]['lte'] && rec[key] > data[key]['lte'])  valid = false
            if(valid) temp.push(rec)
          }
        }
        else{
          for(let rec of res){
            if(compare(rec[key], data[key]))  temp.push(rec)
          }
        }
        res = temp
      }

      if(sort) {
        let sortKey = []
        for (let key in sort) {
          sortKey.push(key)
        }

        res.sort((a, b) => {
          if (data['sort'][sortKey[0]] === 0) {
            return a[sortKey[0]] - b[sortKey[0]]
          } else {
            return b[sortKey[0]] - a[sortKey[0]]
          }
        })
      }

      if(limit){
        res = res.slice(0, limit)
      }
      return {
        code: 20000,
        data: res
      }
    }
  },

  // create
  {
    url: '/vue-element-admin/task-correction/create',
    type: 'post',
    response: config => {
      const { data } = config.body
      if(!data.taskId || !data.userId){
        return {
          code: 678901,
          data: 'taskId or userId cannot be null or empty'
        }
      }

      let exists
      for(let i = 0; i < taskCorrection.length; ++i){
        if(taskCorrection[i].taskId === data.taskId && taskCorrection[i].userId === data.userId){
          exists = true
          break;
        }
      }

      if(!exists){
        teachCourse.push(data)
        return {
          code: 20000,
          data: 'success'
        }
      }

      return {
        code: 123456,
        data: 'error: duplicate!'
      }
    }
  },

  // update
  {
    url: '/vue-element-admin/task-correction/update',
    type: 'post',
    response: config => {
      const { data } = config.body

      let index = -1
      for(let i = 0; i < taskCorrection.length; ++i){
        if(taskCorrection[i].taskId === data.taskId && taskCorrection[i].userId === data.userId){
          index = i
          break;
        }
      }

      if(index >= 0){
        taskCorrection[index] = data
        return {
          code: 20000,
          data: 'success'
        }
      }

      return {
        code: 456778,
        data: 'error: there is no task correction corresponding to task id ' + data.taskId + ' and user id ' + data.userId
      }
    }
  },

  // remove
  {
    url: '/vue-element-admin/task-correction/remove',
    type: 'delete',
    response: config => {
      const { data } = config.body
      if(!data.taskId || !data.userId){
        return {
          code: 678901,
          data: 'error: task id or user id cannot be null or empty'
        }
      }

      let index = -1
      for(let i = 0; i < taskCorrection.length; ++i){
        if(taskCorrection[i].taskId === data.taskId && taskCorrection[i].userId === data.userId){
          index = i
          break;
        }
      }

      if(index >= 0){
        taskCorrection[index] = taskCorrection[0]
        taskCorrection.pop()
        return {
          code: 20000,
          data: 'success'
        }
      }

      return {
        code: 456778,
        data: 'error: there is no task correction with task id ' + data.taskId + ' and user with id ' + data.userId
      }
    }
  }
]
