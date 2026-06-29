<template>
  <div class="app-container nursing-page">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="96px">
      <el-form-item label="护理计划名称" prop="planName">
        <el-input v-model="queryParams.planName" placeholder="请输入护理计划名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="计划状态" clearable>
          <el-option label="启用" :value="1" />
          <el-option label="禁用" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['nursing:plan:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['nursing:plan:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['nursing:plan:remove']">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="planList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="45" align="center" />
      <el-table-column label="序号" align="center" type="index" width="80" />
      <el-table-column label="护理计划名称" align="center" prop="planName" min-width="180" />
      <el-table-column label="排序" align="center" prop="sortNo" width="100" />
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.status" :active-value="1" :inactive-value="0" @change="handleStatusChange(scope.row)" />
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" min-width="180" show-overflow-tooltip />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)">详情</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['nursing:plan:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" class="danger-text" @click="handleDelete(scope.row)" v-hasPermi="['nursing:plan:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="860px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="108px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="计划名称" prop="planName">
              <el-input v-model="form.planName" placeholder="请输入护理计划名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序" prop="sortNo">
              <el-input-number v-model="form.sortNo" controls-position="right" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>

        <div class="sub-title">
          <span>护理项目明细</span>
          <el-button type="primary" plain size="mini" icon="el-icon-plus" @click="addProjectPlan">添加项目</el-button>
        </div>
        <el-table :data="form.projectPlans" border size="small">
          <el-table-column label="护理项目" min-width="180">
            <template slot-scope="scope">
              <el-select v-model="scope.row.projectId" placeholder="请选择护理项目" filterable style="width: 100%">
                <el-option v-for="item in projectOptions" :key="item.value" :label="item.label" :value="Number(item.value)" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="执行周期" width="130">
            <template slot-scope="scope">
              <el-select v-model="scope.row.executeCycle" style="width: 100%">
                <el-option label="天" :value="0" />
                <el-option label="周" :value="1" />
                <el-option label="月" :value="2" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="执行频次" width="120">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.executeFrequency" :min="1" controls-position="right" style="width: 100%" />
            </template>
          </el-table-column>
          <el-table-column label="执行时间" width="150">
            <template slot-scope="scope">
              <el-time-picker v-model="scope.row.executeTime" value-format="HH:mm:ss" placeholder="时间" style="width: 100%" />
            </template>
          </el-table-column>
          <el-table-column label="备注" min-width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.remark" placeholder="备注" />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80" align="center">
            <template slot-scope="scope">
              <el-button type="text" class="danger-text" @click="removeProjectPlan(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="护理计划详情" :visible.sync="detailOpen" width="760px" append-to-body>
      <el-descriptions :column="3" border>
        <el-descriptions-item label="计划名称">{{ detail.planName }}</el-descriptions-item>
        <el-descriptions-item label="排序">{{ detail.sortNo }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ detail.status === 1 ? '启用' : '禁用' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="3">{{ detail.remark }}</el-descriptions-item>
      </el-descriptions>
      <el-table :data="detail.projectPlans || []" border size="small" style="margin-top: 14px">
        <el-table-column label="护理项目" prop="projectName" />
        <el-table-column label="执行周期" width="100">
          <template slot-scope="scope">{{ cycleText(scope.row.executeCycle) }}</template>
        </el-table-column>
        <el-table-column label="执行频次" prop="executeFrequency" width="100" />
        <el-table-column label="执行时间" prop="executeTime" width="130" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import { listPlan, getPlan, delPlan, addPlan, updatePlan } from '@/api/nursing/plan'
import { getAllProjects } from '@/api/nursing/project'

export default {
  name: 'NursingPlan',
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      planList: [],
      projectOptions: [],
      title: '',
      open: false,
      detailOpen: false,
      detail: {},
      queryParams: { pageNum: 1, pageSize: 10, planName: undefined, status: undefined },
      form: {},
      rules: {
        planName: [{ required: true, message: '护理计划名称不能为空', trigger: 'blur' }],
        sortNo: [{ required: true, message: '排序不能为空', trigger: 'blur' }],
        status: [{ required: true, message: '状态不能为空', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
    this.getProjectOptions()
  },
  methods: {
    cycleText(value) {
      return value === 1 ? '周' : value === 2 ? '月' : '天'
    },
    getProjectOptions() {
      getAllProjects().then(res => {
        this.projectOptions = res.data || []
      })
    },
    getList() {
      this.loading = true
      listPlan(this.queryParams).then(response => {
        this.planList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    reset() {
      this.form = { id: undefined, planName: undefined, sortNo: 0, status: 1, remark: undefined, projectPlans: [] }
      this.resetForm('form')
    },
    cancel() {
      this.open = false
      this.reset()
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = selection.length !== 1
    },
    handleAdd() {
      this.reset()
      this.addProjectPlan()
      this.open = true
      this.title = '新增护理计划'
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getPlan(id).then(response => {
        this.form = response.data
        this.form.projectPlans = this.form.projectPlans || []
        this.open = true
        this.title = '修改护理计划'
      })
    },
    handleDetail(row) {
      getPlan(row.id).then(response => {
        this.detail = response.data
        this.detailOpen = true
      })
    },
    addProjectPlan() {
      this.form.projectPlans.push({ projectId: undefined, executeCycle: 0, executeFrequency: 1, executeTime: '08:00:00', remark: undefined })
    },
    removeProjectPlan(index) {
      this.form.projectPlans.splice(index, 1)
    },
    handleStatusChange(row) {
      const text = row.status === 1 ? '启用' : '禁用'
      this.$modal.confirm('确认要' + text + '“' + row.planName + '”吗？').then(() => {
        return updatePlan(row)
      }).then(() => {
        this.$modal.msgSuccess(text + '成功')
      }).catch(() => {
        row.status = row.status === 1 ? 0 : 1
      })
    },
    submitForm() {
      if (!this.form.projectPlans || this.form.projectPlans.length === 0) {
        this.$modal.msgError('请至少添加一个护理项目')
        return
      }
      this.$refs.form.validate(valid => {
        if (valid) {
          const request = this.form.id !== undefined ? updatePlan(this.form) : addPlan(this.form)
          request.then(() => {
            this.$modal.msgSuccess(this.form.id !== undefined ? '修改成功' : '新增成功')
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除护理计划编号为 "' + ids + '" 的数据项？').then(function() {
        return delPlan(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.sub-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: 8px 0 10px;
  font-weight: 600;
}
.danger-text {
  color: #f56c6c;
}
</style>
