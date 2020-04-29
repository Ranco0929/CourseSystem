import request from '@/utils/request'

export function create(ns, data) {
  return request({
    url: '/vue-element-admin/*/create'.replace('*', ns),
    method: 'post',
    data
  })
}

export function update(ns, data) {
  return request({
    url: '/vue-element-admin/*/update'.replace('*', ns),
    method: 'post',
    data
  })
}

export function find(ns, data) {
  return request({
    url: '/vue-element-admin/*/find'.replace('*', ns),
    method: 'post',
    data
  })
}

export function remove(ns, data) {
  return request({
    url: '/vue-element-admin/*/remove'.replace('*', ns),
    method: 'delete',
    data
  })
}
