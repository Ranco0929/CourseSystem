import { user } from './db'
import { generateToken } from './token-manager'
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
          index = i
          break
        }
      }

      if(index >= 0 && user[index].password === password){
        const ret = {
          userId: user[index].userId,
          name: user[index].name,
          info: user[index].info,
          email: user[index].email,
          token: generateToken(user[index].email),
          verified: user[index].verified,
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
        code: 40001,
        message: 'invalid account or password'
      }
    }
  },

  // get user info
  {
    url: '/vue-element-admin/user/get_info',
    type: 'get',
    response: config => {
      const token = config.headers['x-token']

      let index = -1
      for(let i = 0; i < user.length; ++i){
        if(user[i].email === token.email){
          index = i
          break
        }
      }

      if (index < 0) {
        return {
          code: 50000,
          message: 'server error.'
        }
      }

      const ret = {
        userId: user[index].userId,
        name: user[index].name,
        info: user[index].info,
        email: user[index].email,
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
        data: {}
      }
    }
  },

  // register
  {
    url: '/vue-element-admin/user/register',
    type: 'post',
    response: config => {
      const { name, email, info, role, password } = config.body

      if(name && email && role && password){
        if(role !== 'student' && role !== 'teacher'){
          return {
            code: 40000,
            msg: 'role can only be student or teacher'
          }
        }

        let index = -1
        for(let i = 0; i < user.length; ++i){
          if(user[i].email === email){
            index = i
            break
          }
        }
        if(index >= 0){
          return {
            code: 40006,
            msg: "duplicate email,please choose another email to register"
          }
        }

        const newUser = {}
        newUser['userId'] = uuid()
        newUser['name'] = name
        newUser['email'] = email
        newUser['info'] = info
        newUser['role'] = role
        newUser['password'] = password
        newUser['verified'] = false
        newUser['createdAt'] = new Date().toUTCString()
        newUser['updatedAt'] = newUser['createdAt']

        user.push(Object.assign({}, newUser, {verifiedCode: '111111'}))
        const ret = Object.assign({}, newUser)
        delete ret['password']
        delete ret['verified']
        return {
          code: 20000,
          data: ret
        }
      }
      return {
        code: 40000,
        msg: "name or email or role or password cannot be null"
      }
    }
  },

  // verify
  {
    url: '/vue-element-admin/user/verify',
    type: 'post',
    response: config => {
      const { userId, verifiedCode } = config.body

      let index = -1
      for(let i = 0; i < user.length; ++i){
        if(user[i].userId === userId){
          index = i
          break
        }
      }

      if(index < 0){
        return {
          code: 40004,
          msg: 'there is no user corresponding to the user id'
        }
      }

      if(user[index].verfiedCode !== verifiedCode){
        return {
          code: 40022,
          msg: 'wrong verified code'
        }
      }

      return {
        code: 20000,
        data:{}
      }
    }
  },

  // edit info
  {
    url: '/vue-element-admin/user/edit_info',
    type: 'post',
    response: config => {
      const { userId, name, info } = config.body
      if(!userId || !name){
        return {
          code: 40000,
          data: 'error: user id or name cannot be null'
        }
      }

      let index = -1
      for(let i = 0; i < user.length; ++i){
        if(user[i].userId === userId){
          index = i
          break;
        }
      }

      if(index < 0){
        return {
          code: 40004,
          msg: 'there is no user corresponding to user id ' + userId
        }
      }

      user[index]['name'] = name
      user[index]['info'] = info
      const ret = Object.assign({}, user[index])
      delete ret['verifiedCode']
      return {
        code: 20000,
        data: ret
      }
    }
  },
  {
    url: '/vue-element-admin/user/get_teachers',
    type: 'get',
    response: config => {
      const teachers = []
      for(let u of user){
        if(u['role'] === 'teacher'){
          teachers.push(u)
        }
      }
      return {
        code: 20000,
        data: teachers
      }
    }
  }
]
