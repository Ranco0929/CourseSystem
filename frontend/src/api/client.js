import request from '@/utils/request'

function create(ns, data) {
  return request({
    url: '/vue-element-admin/*/create'.replace('*', ns),
    method: 'post',
    data
  })
}

function update(ns, data) {
  return request({
    url: '/vue-element-admin/*/update'.replace('*', ns),
    method: 'post',
    data
  })
}

function find(ns, data) {
  return request({
    url: '/vue-element-admin/*/find'.replace('*', ns),
    method: 'post',
    data
  })
}

function remove(ns, data) {
  return request({
    url: '/vue-element-admin/*/remove'.replace('*', ns),
    method: 'delete',
    data
  })
}

module.exports = {
  create,
  update,
  find,
  remove
}
