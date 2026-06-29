<template>
  <div class="app-container task-page">
    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" class="task-filter">
      <el-form-item label="老人姓名" prop="elderName">
        <el-input v-model="queryParams.elderName" placeholder="请输入" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="护理员姓名" prop="caregiverId">
        <el-select v-model="queryParams.caregiverId" placeholder="请选择" clearable filterable>
          <el-option v-for="item in caregivers" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="护理项目" prop="projectId">
        <el-select v-model="queryParams.projectId" placeholder="请选择" clearable filterable>
          <el-option v-for="item in projects" :key="item.value" :label="item.label" :value="Number(item.value)" />
        </el-select>
      </el-form-item>
      <el-form-item label="期望服务时间">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          value-format="yyyy-MM-dd"
          range-separator="-"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
        />
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
      </el-form-item>
    </el-form>

    <div class="status-tabs">
      <el-radio-group v-model="queryParams.status" size="small" @change="handleQuery">
        <el-radio-button :label="0">待执行</el-radio-button>
        <el-radio-button :label="1">已执行</el-radio-button>
        <el-radio-button :label="2">已取消</el-radio-button>
      </el-radio-group>
    </div>

    <el-table v-loading="loading" :data="taskList">
      <el-table-column label="序号" type="index" width="65" align="center" />
      <el-table-column label="老人姓名" prop="elderName" min-width="95" />
      <el-table-column label="床位号" prop="bedNo" width="85" align="center" />
      <el-table-column label="护理项目名称" prop="projectName" min-width="120" />
      <el-table-column label="项目类型" width="100" align="center">
        <template slot-scope="scope">
          <el-tag size="mini" :type="scope.row.taskType === 0 ? 'success' : 'warning'" effect="plain">
            {{ scope.row.taskType === 0 ? '护理计划内' : '护理计划外' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="护理员姓名" prop="caregiverName" min-width="105" />
      <el-table-column label="期望服务时间" prop="expectedServiceTime" min-width="165" />
      <el-table-column v-if="queryParams.status === 1" label="执行时间" prop="executeTime" min-width="165" />
      <el-table-column v-else-if="queryParams.status === 2" label="取消时间" prop="cancelTime" min-width="165" />
      <el-table-column v-else label="创建时间" prop="createTime" min-width="165" />
      <el-table-column label="操作" align="center" :width="queryParams.status === 0 ? 240 : 80">
        <template slot-scope="scope">
          <template v-if="queryParams.status === 0">
            <el-button type="text" class="danger-link" @click="openCancel(scope.row)">取消</el-button>
            <el-button type="text" @click="viewTask(scope.row)">查看</el-button>
            <el-button type="text" @click="openExecute(scope.row)">执行</el-button>
            <el-button type="text" @click="openReschedule(scope.row)">改期</el-button>
            <el-button type="text" @click="openCaregiver(scope.row)">换人</el-button>
          </template>
          <el-button v-else type="text" @click="viewTask(scope.row)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog title="填写取消原因" :visible.sync="cancelOpen" width="480px" append-to-body>
      <el-form ref="cancelForm" :model="actionForm" :rules="cancelRules" label-width="100px">
        <el-form-item label="取消原因" prop="cancelReason">
          <el-input v-model="actionForm.cancelReason" type="textarea" :rows="4" maxlength="100" show-word-limit />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="cancelOpen = false">取消</el-button>
        <el-button type="primary" @click="confirmCancel">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="新增执行记录" :visible.sync="executeOpen" width="560px" append-to-body>
      <el-form ref="executeForm" :model="actionForm" :rules="executeRules" label-width="100px">
        <el-form-item label="执行时间" prop="executeTime">
          <el-date-picker v-model="actionForm.executeTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%" />
        </el-form-item>
        <el-form-item label="执行图片" prop="executeImage">
          <image-upload v-model="actionForm.executeImage" :limit="1" />
        </el-form-item>
        <el-form-item label="执行记录" prop="executeRecord">
          <el-input v-model="actionForm.executeRecord" type="textarea" :rows="4" maxlength="100" show-word-limit />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="executeOpen = false">取消</el-button>
        <el-button type="primary" @click="confirmExecute">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="修改执行时间" :visible.sync="rescheduleOpen" width="480px" append-to-body>
      <el-form ref="rescheduleForm" :model="actionForm" :rules="rescheduleRules" label-width="120px">
        <el-form-item label="期望服务时间" prop="expectedServiceTime">
          <el-date-picker v-model="actionForm.expectedServiceTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="rescheduleOpen = false">取消</el-button>
        <el-button type="primary" @click="confirmReschedule">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="更换护理员" :visible.sync="caregiverOpen" width="460px" append-to-body>
      <el-form label-width="100px">
        <el-form-item label="护理员" required>
          <el-select v-model="actionForm.caregiverId" placeholder="请选择护理员" style="width: 100%">
            <el-option v-for="item in caregivers" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="caregiverOpen = false">取消</el-button>
        <el-button type="primary" @click="confirmCaregiver">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listTask, cancelTask, executeTask, rescheduleTask, changeTaskCaregiver } from '@/api/nursing/task'
import { listCaregivers } from '@/api/nursing/responsible'
import { getAllProjects } from '@/api/nursing/project'

function nowText() {
  const d = new Date()
  const pad = value => String(value).padStart(2, '0')
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
}

export default {
  name: 'NursingTask',
  data() {
    return {
      loading: false,
      total: 0,
      taskList: [],
      caregivers: [],
      projects: [],
      dateRange: [],
      cancelOpen: false,
      executeOpen: false,
      rescheduleOpen: false,
      caregiverOpen: false,
      actionForm: {},
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        elderName: '',
        caregiverId: undefined,
        projectId: undefined,
        status: 0
      },
      cancelRules: {
        cancelReason: [{ required: true, message: '请输入取消原因', trigger: 'blur' }]
      },
      executeRules: {
        executeTime: [{ required: true, message: '请选择执行时间', trigger: 'change' }],
        executeImage: [{ required: true, message: '请上传执行图片', trigger: 'change' }],
        executeRecord: [{ required: true, message: '请输入执行记录', trigger: 'blur' }]
      },
      rescheduleRules: {
        expectedServiceTime: [{ required: true, message: '请选择期望服务时间', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
    listCaregivers().then(res => { this.caregivers = res.data || [] })
    getAllProjects().then(res => { this.projects = res.data || [] })
  },
  methods: {
    getList() {
      this.loading = true
      const params = this.addDateRange({ ...this.queryParams }, this.dateRange)
      listTask(params).then(res => {
        this.taskList = res.rows || []
        this.total = res.total || 0
      }).finally(() => {
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.dateRange = []
      this.resetForm('queryForm')
      this.queryParams.status = 0
      this.handleQuery()
    },
    viewTask(row) {
      this.$router.push(`/nursing/task/detail/${row.id}`)
    },
    openCancel(row) {
      this.actionForm = { id: row.id, cancelReason: '' }
      this.cancelOpen = true
    },
    confirmCancel() {
      this.$refs.cancelForm.validate(valid => {
        if (!valid) return
        cancelTask(this.actionForm).then(() => {
          this.$modal.msgSuccess('任务已取消')
          this.cancelOpen = false
          this.getList()
        })
      })
    },
    openExecute(row) {
      this.actionForm = { id: row.id, executeTime: nowText(), executeImage: '', executeRecord: '' }
      this.executeOpen = true
    },
    confirmExecute() {
      this.$refs.executeForm.validate(valid => {
        if (!valid) return
        executeTask(this.actionForm).then(() => {
          this.$modal.msgSuccess('执行记录提交成功')
          this.executeOpen = false
          this.getList()
        })
      })
    },
    openReschedule(row) {
      this.actionForm = { id: row.id, expectedServiceTime: row.expectedServiceTime }
      this.rescheduleOpen = true
    },
    confirmReschedule() {
      this.$refs.rescheduleForm.validate(valid => {
        if (!valid) return
        rescheduleTask(this.actionForm).then(() => {
          this.$modal.msgSuccess('任务时间修改成功')
          this.rescheduleOpen = false
          this.getList()
        })
      })
    },
    openCaregiver(row) {
      this.actionForm = { id: row.id, caregiverId: row.caregiverId }
      this.caregiverOpen = true
    },
    confirmCaregiver() {
      if (!this.actionForm.caregiverId) {
        this.$modal.msgWarning('请选择护理员')
        return
      }
      changeTaskCaregiver(this.actionForm.id, this.actionForm.caregiverId).then(() => {
        this.$modal.msgSuccess('护理员更换成功')
        this.caregiverOpen = false
        this.getList()
      })
    }
  }
}
</script>

<style scoped lang="scss">
.task-page {
  .task-filter {
    padding: 4px 0 0;
    border-bottom: 1px solid #e8ebf0;
  }
  .status-tabs {
    padding: 14px 0;
  }
  .danger-link {
    color: #f56c6c;
  }
}
</style>
