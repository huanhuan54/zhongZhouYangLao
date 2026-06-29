import request from '@/utils/request'

export function listRecords(module, query) {
  return request({
    url: `/operations/${module}/list`,
    method: 'get',
    params: query
  })
}

export function getRecord(module, id) {
  return request({
    url: `/operations/${module}/${id}`,
    method: 'get'
  })
}

export function addRecord(module, data) {
  return request({
    url: `/operations/${module}`,
    method: 'post',
    data
  })
}

export function updateRecord(module, data) {
  return request({
    url: `/operations/${module}`,
    method: 'put',
    data
  })
}

export function updateRecordStatus(module, id, status) {
  return request({
    url: `/operations/${module}/${id}/status`,
    method: 'put',
    data: { status }
  })
}

export function deleteRecord(module, id) {
  return request({
    url: `/operations/${module}/${id}`,
    method: 'delete'
  })
}
