const uuid = require('uuid').v4

const user= [
  {
    userId: uuid(),
    name: '张三',
    info: '这是个无趣的人',
    email: '000000@qq.com',
    password: '000000',
    verified: true,
    verifiedCode: '614870',
    role: 'teacher',
    createdAt: '2018-09-21',
    updatedAt: '2019-01-01'
  },
  {
    userId: uuid(),
    name: '李四',
    info: '这是个有趣的人',
    email: '000001@qq.com',
    password: '000000',
    verified: true,
    verifiedCode: '676667',
    role: 'teacher',
    createdAt: '2012-09-21',
    updatedAt: '2014-01-01'
  },
  {
    userId: uuid(),
    name: '小波',
    info: '这是个无趣的人',
    email: '17030120000@qq.com',
    password: '000000',
    verified: true,
    verifiedCode: '123456',
    role: 'student',
    createdAt: '2018-09-21',
    updatedAt: '2019-01-01'
  },
  {
    userId: uuid(),
    name: '小月',
    info: '这是个有趣的人',
    email: '17030120001@qq.com',
    password: '000000',
    verified: true,
    verifiedCode: '789012',
    role: 'student',
    createdAt: '2012-09-21',
    updatedAt: '2014-01-01'
  }
]

const course = [
  {
    courseId: uuid(),
    name: '操作系统',
    info: '无',
    creator: user[0].userId,
    avatar: 'https://www.myfreax.com/content/images/2019/07/linux-ip-4.png',
    createdAt: '2018-09-21',
    updatedAt: '2019-01-01'
  },
  {
    courseId: uuid(),
    name: '数据库',
    info: '无',
    creator: user[1].userId,
    avatar: 'https://pic.90sjimg.com/design/00/67/59/63/58e89bf380532.png',
    createdAt: '2012-09-21',
    updatedAt: '2014-01-01'
  }
]

const teachCourse = [
  {
    userId: user[0].userId,
    courseId: course[0].courseId,
    createdAt: '2020-02-03'
  },
  {
    userId: user[1].userId,
    courseId: course[1].courseId,
    createdAt: '2020-02-03'
  }
]

const selectCourse = [
  {
    userId: user[2].userId,
    courseId: course[0].courseId,
    createdAt: '2020-02-05'
  },
  {
    userId: user[3].userId,
    courseId: course[0].courseId,
    createdAt: '2020-02-05'
  },
  {
    userId: user[2].userId,
    courseId: course[1].courseId,
    createdAt: '2020-02-05'
  },
  {
    userId: user[3].userId,
    courseId: course[1].courseId,
    createdAt: '2020-02-05'
  }
]

const task = [
  {
    taskId: uuid(),
    courseId: course[0].courseId,
    title: course[0].name + '第一次作业',
    content: {
      '0':{
        type: '0',// 0：单选， 1：多选， 2：填空题， 3：判断题， 4：简答题， 5：应用题
        question: {
          '0':{
            type: 'text',
            content: '给出下列三个选项，请问哪一个是最合理的？\nA.你说合理就合理\nB。你说合理也不合理\nC.要具体看才能知道到底合不合理\n'
          },
          '1':{
            type: 'image',
            content: 'https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1587735242&di=f4bada181671944549f6db2e6cdd62a4&src=http://cdnimg103.lizhi.fm/audio_cover/2015/07/26/21721885415852551_320x320.jpg'
          }
        },
        grade: '5'
      }
    },
    solution:{
      '0':{
        '0':{
          type: 'text',
          content: 'C'
        }
      }
    },
    state: '1',
    deadline: '2020-02-28',
    createdAt: '2020-02-09',
    updatedAt: '2020-02-09'
  },
  {
    taskId: uuid(),
    courseId: course[1].courseId,
    title: course[1].name + '第一次作业',
    content: {
      '0':{
        type: '0',// 0：单选， 1：多选， 2：填空题， 3：判断题， 4：简答题， 5：应用题
        question: {
          '0':{
            type: 'text',
            content: '给出下列三个选项，请问哪一个是最合理的？A.你说合理就合理B.你说合理也不合理C.要具体看才能知道到底合不合理'
          },
          '1':{
            type: 'image',
            content: 'https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1587735242&di=f4bada181671944549f6db2e6cdd62a4&src=http://cdnimg103.lizhi.fm/audio_cover/2015/07/26/21721885415852551_320x320.jpg'
          }
        },
        grade: '5'
      }
    },
    solution:{
      '0':{
        '0':{
          type: 'text',
          content: 'C'
        }
      }
    },
    state: '1',
    deadline: '2020-02-28',
    createdAt: '2020-02-09',
    updatedAt: '2020-02-09'
  }
]

const taskSubmission = [
  {
    taskId: task[1].taskId,
    userId: user[2].userId,
    answer: {
      '0':{
        '0':{
          type: 'text',
          content: 'B'
        }
      }
    },
    state: '1',
    createdAt: '2020-02-09',
    updatedAt: '2020-02-10'
  },
  {
    taskId: task[1].taskId,
    userId: user[3].userId,
    answer: {
      '0':{
        '0':{
          type: 'text',
          content: 'B'
        }
      }
    },
    state: '1',
    createdAt: '2020-02-09',
    updatedAt: '2020-02-10'
  },
  {
    taskId: task[0].taskId,
    userId: user[2].userId,
    answer: {
      '0':{
        '0':{
          type: 'text',
          content: 'B'
        }
      }
    },
    state: '1',
    createdAt: '2020-02-09',
    updatedAt: '2020-02-10'
  },
  {
    taskId: task[0].taskId,
    userId: user[3].userId,
    answer: {
      '0':{
        '0':{
          type: 'text',
          content: 'B'
        }
      }
    },
    state: '1',
    createdAt: '2020-02-09',
    updatedAt: '2020-02-10'
  },
]

const taskCorrection = [
  {
    taskId: task[0].taskId,
    userId: user[2].userId,
    content: {
      '0':{
        grade: '3',
        extra: '正确'
      }
    },
    state: '1',
    createdAt: '2020-02-11',
    updatedAt: '2020-02-11'
  }
]

const taskAnalysis = [
  {
    taskId: task[0].taskId,
    trueOrFalse:{
      '0':{
        'true': '1',
        'false': '0'
      }
    },
    duplicate:{
      // TODO
    }
  }
]

module.exports = {
  user,
  course,
  teachCourse,
  selectCourse,
  task,
  taskSubmission,
  taskCorrection,
  taskAnalysis
}
