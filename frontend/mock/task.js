const task = require('./db').task
const uuid = require('uuid').v4

export default [
  // find
  {
    url: '/vue-element-admin/task/find',
    type: 'post',
    response: config => {
      const { data } = config.body
      let limit = null
      if(data.limit !== undefined) limit = data.limit
      let sort = null
      if(data.sort !== undefined) sort = data.sort
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

      let res = task
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
    url: '/vue-element-admin/task/create',
    type: 'post',
    response: config => {
      const { data } = config.body
      data['taskId'] = uuid()
      task.push(data)
      return {
        code: 20000,
        data: 'success'
      }
    }
  },

  // update
  {
    url: '/vue-element-admin/task/update',
    type: 'post',
    response: config => {
      const { data } = config.body

      if(!data.taskId){
        return {
          code: 678901,
          data: 'error: task id cannot be null or empty'
        }
      }

      let index = -1
      for(let i = 0; i < task.length; ++i){
        if(task[i].taskId === data.taskId){
          index = i
          break;
        }
      }

      if(index >= 0){
        data['taskId'] = data.taskId;
        task[index] = data
        return {
          code: 20000,
          data: 'success'
        }
      }

      return {
        code: 456778,
        data: 'error: there is no task corresponding to id ' + data.taskId
      }
    }
  },

  // remove
  {
    url: '/vue-element-admin/task/remove',
    type: 'delete',
    response: config => {
      const { data } = config.body
      if(!data.taskId){
        return {
          code: 678901,
          data: 'error: task id cannot be null or empty'
        }
      }

      let index = -1
      for(let i = 0; i < task.length; ++i){
        if(task[i].taskId === data.taskId){
          index = i
          break;
        }
      }

      if(index >= 0){
        task[index] = task[0]
        task.pop()
        return {
          code: 20000,
          data: 'success'
        }
      }

      return {
        code: 456778,
        data: 'error: there is no task corresponding to id ' + data.taskId
      }
    }
  }
]
