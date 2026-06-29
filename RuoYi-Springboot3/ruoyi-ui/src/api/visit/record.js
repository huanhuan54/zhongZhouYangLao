import request from '@/utils/request'

export function listVisitRecords(query) {
  return request({
    url: '/visit/record/list',
    method: 'get',
    params: query
  })
}

export function addVisitRecord(data) {
  return request({
    url: '/visit/record',
    method: 'post',
    data
  })
}
