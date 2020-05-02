import { user } from './db'
import uuid from 'uuid/v4'

export default [
  // user login
  {
    url: '/vue-element-admin/user/login',
    type: 'post',
    response: config => {
      const { username, password } = config.body
      let index = -1
      for(let i = 0; i < user.length; ++i){
        if(user[i].email === username){
          index = i;
          break;
        }
      }

      if(index >= 0 && user[index].password === password){
        const ret = {
            userId: user[index].userId,
            name: user[index].name,
            info: user[index].info,
            avatar: user[index].avatar,
            email: user[index].email,
            token: user[index].token,
            verifiedCode: user[index].verifiedCode,
            role: user[index].role,
            createdAt: user[index].createdAt,
            updatedAt: user[index].updatedAt
          }
        return {
          code: 20000,
          data: ret
        }
      }

      return {
        code: 60204,
        message: 'Account and password are incorrect.'
      }
    }
  },

  // get user info
  {
    url: '/vue-element-admin/user/info\.*',
    type: 'get',
    response: config => {
      const { token } = config.query
      let index = -1
      for(let i = 0; i < user.length; ++i){
        if(user[i].token === token){
          index = i;
          break;
        }
      }

      // mock error
      if (index < 0) {
        return {
          code: 50008,
          message: 'Login failed, unable to get user details.'
        }
      }

      const ret = {
        userId: user[index].userId,
        name: user[index].name,
        info: user[index].info,
        avatar: user[index].avatar,
        email: user[index].email,
        token: user[index].token,
        verifiedCode: user[index].verifiedCode,
        role: user[index].role,
        createdAt: user[index].createdAt,
        updatedAt: user[index].updatedAt
      }
      return {
        code: 20000,
        data: ret
      }
    }
  },

  // user logout
  {
    url: '/vue-element-admin/user/logout',
    type: 'post',
    response: _ => {
      return {
        code: 20000,
        data: 'success'
      }
    }
  },

  // create
  {
    url: '/vue-element-admin/user/create',
    type: 'post',
    response: config => {
      const { data } = config.body

      data['userId'] = uuid()
      user.push(data)
      return {
        code: 20000,
        data: 'success'
      }
    }
  },

  // find
  {
    url: '/vue-element-admin/user/find',
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

      let res = user
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

      const ret = []
      for(let i = 0; i < res.length; ++i){
        ret.push({
          userId: res[i].userId,
          name: res[i].name,
          info: res[i].info,
          avatar: res[i].avatar,
          email: res[i].email,
          role: res[i].role,
          createdAt: res[i].createdAt,
          updatedAt: res[i].updatedAt
        })
      }
      return {
        code: 20000,
        data: ret
      }
    }
  },

  // update
  {
    url: '/vue-element-admin/user/update',
    type: 'post',
    response: config => {
      const { data } = config.body
      if(!data.userId){
        return {
          code: 456778,
          data: 'error: user id cannot be empty'
        }
      }

      let index = -1
      for(let i = 0; i < user.length; ++i){
        if(user[i].userId === data.userId){
          index = i
          break;
        }
      }

      if(index >= 0){
        user[index] = data
        return {
          code: 20000,
          data: 'success'
        }
      }

      return {
        code: 456778,
        data: 'error: there is no user corresponding to id ' + data.userId
      }
    }
  },
]
