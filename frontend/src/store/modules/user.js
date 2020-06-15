import { get, post } from '@/api/client'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { resetRouter } from '@/router'

const state = {
  token: getToken(),
  userId: '',
  email: '',
  name: '',
  info: '',
  roles: ''
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_USER_ID: (state, userId) => {
    state.userId = userId
  },
  SET_INFO: (state, info) => {
    state.info = info
  },
  SET_EMAIL: (state, email) => {
    state.email = email
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      post('user/login', { username: username.trim(), password: password }).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        console.log('succeed login')
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  info({ commit }) {
    return new Promise((resolve, reject) => {
      get('user/get_info', {}).then(response => {
        const { data } = response

        if (!data) {
          reject('Verification failed, please Login again.')
        }

        const { role, name, info, email, userId } = data

        // roles must be a non-empty array
        if (!role) {
          reject('getInfo: roles must be a non-null!')
        }
        commit('SET_USER_ID', userId)
        commit('SET_ROLES', role)
        commit('SET_EMAIL', email)
        commit('SET_NAME', name)
        commit('SET_INFO', info)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, dispatch }) {
    return new Promise((resolve, reject) => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', '')
      removeToken()
      resetRouter()

      // reset visited views and cached views
      // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
      dispatch('tagsView/delAllViews', null, { root: true })
      resolve()
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      removeToken()
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
