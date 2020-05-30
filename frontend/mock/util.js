function filterUserInfo(userInfo) {
  if(!userInfo){
    return {}
  }
  const ret = {
    userId: userInfo['userId'],
    name: userInfo['name'],
    info: userInfo['info'],
    email: userInfo['email'],
    role: userInfo['role'],
    createdAt: userInfo['createdAt'],
    updatedAt: userInfo['updatedAt']
  }
  return ret
}

module.exports = {
  filterUserInfo
}
