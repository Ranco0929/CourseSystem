import request from '@/utils/request'

function get(ns, params) {
  return request({
    url: '/vue-element-admin/*'.replace('*', ns),
    method: 'get',
    params
  })
}

function post(ns, data) {
  return request({
    url: '/vue-element-admin/*'.replace('*', ns),
    method: 'post',
    data
  })
}

module.exports = {
  get,
  post
}
