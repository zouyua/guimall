import axios from "@/axios"

export function uploadFile(file) {
  const formData = new FormData()
  formData.append('file', file)
  return axios.post('/file/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
    timeout: 30000
  })
}
