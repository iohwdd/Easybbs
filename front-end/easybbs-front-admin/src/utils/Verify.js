const regs = {
  email: /^[A-Za-z0-9]+([_\.][A-Za-z0-9]+)*@qq.com$/,
  number: /^([0]|[1-9][0-9]*)$/,
  password: /^(?![a-zA-Z]+$)(?!\d+$)(?![^\da-zA-Z\s]+$).{8,15}$/
}
const verify = (rule, value, reg, callback) => {
  if (value) {
    if (reg.test(value)) {
      callback()
    } else {
      callback(new Error(rule.message))
    }
  } else {
    callback()
  }
}
export default {
  email: (rule, value, callback) => {
    return verify(rule, value, regs.email, callback)
  },
  number: (rule, value, callback) => {
    return verify(rule, value, regs.number, callback)
  },
  password: (rule, value, callback) => {
    return verify(rule, value, regs.password, callback)
  }
}
