import { user } from './db'

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
  }
]
