<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="80px">
      <el-form-item label="等级名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入等级名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="等级状态" clearable>
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
        <el-button
          v-hasPermi="['nursing:level:add']"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['nursing:level:edit']"
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['nursing:level:remove']"
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="levelList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" type="index" width="80" align="center">
        <template slot-scope="scope">
          <span>{{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="等级名称" prop="name" min-width="150" />
      <el-table-column label="关联计划" prop="planName" min-width="150" />
      <el-table-column label="护理费用" prop="fee" width="120" align="center">
        <template slot-scope="scope">￥{{ scope.row.fee }}</template>
      </el-table-column>
      <el-table-column label="状态" prop="status" width="120" align="center">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value="1"
            :inactive-value="0"
            @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="等级说明" prop="description" min-width="220" show-overflow-tooltip />
      <el-table-column label="操作" align="center" width="190" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)">详情</el-button>
          <el-button
            v-hasPermi="['nursing:level:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            v-hasPermi="['nursing:level:remove']"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>
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

    <el-dialog :title="title" :visible.sync="open" width="620px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="等级名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入等级名称" />
        </el-form-item>
        <el-form-item label="护理计划" prop="lplanId">
          <el-select v-model="form.lplanId" placeholder="请选择护理计划" clearable style="width: 100%">
            <el-option
              v-for="plan in planOptions"
              :key="plan.id"
              :label="plan.planName"
              :value="plan.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="护理费用" prop="fee">
          <el-input-number v-model="form.fee" :min="0" :precision="2" :step="50" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="等级状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="等级说明" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请输入等级说明" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="护理等级详情" :visible.sync="detailOpen" width="640px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="等级名称">{{ detail.name }}</el-descriptions-item>
        <el-descriptions-item label="关联计划">{{ detail.planName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="护理费用">￥{{ detail.fee }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ detail.status === 1 ? '启用' : '禁用' }}</el-descriptions-item>
        <el-descriptions-item label="等级说明" :span="2">{{ detail.description || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { listLevel, getLevel, delLevel, addLevel, updateLevel } from '@/api/nursing/level'
import { getAllPlans } from '@/api/nursing/plan'

export default {
  name: 'NursingLevel',
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      levelList: [],
      planOptions: [],
      title: '',
      open: false,
      detailOpen: false,
      detail: {},
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        status: null
      },
      form: {},
      rules: {
        name: [{ required: true, message: '等级名称不能为空', trigger: 'blur' }],
        lplanId: [{ required: true, message: '护理计划不能为空', trigger: 'change' }],
        fee: [{ required: true, message: '护理费用不能为空', trigger: 'blur' }],
        status: [{ required: true, message: '等级状态不能为空', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
    this.getPlanOptions()
  },
  methods: {
    getList() {
      this.loading = true
      listLevel(this.queryParams).then(response => {
        this.levelList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getPlanOptions() {
      getAllPlans().then(response => {
        this.planOptions = response.data || []
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        id: null,
        name: null,
        lplanId: null,
        fee: 0,
        status: 1,
        description: null
      }
      this.resetForm('form')
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
      this.multiple = !selection.length
    },
    handleStatusChange(row) {
      const text = row.status === 1 ? '启用' : '禁用'
      this.$modal.confirm('确认要' + text + '"' + row.name + '"护理等级吗？').then(() => {
        return updateLevel(row)
      }).then(() => {
        this.$modal.msgSuccess(text + '成功')
      }).catch(() => {
        row.status = row.status === 1 ? 0 : 1
      })
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加护理等级'
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids[0]
      getLevel(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改护理等级'
      })
    },
    handleDetail(row) {
      getLevel(row.id).then(response => {
        this.detail = response.data
        this.detailOpen = true
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateLevel(this.form).then(() => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addLevel(this.form).then(() => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除护理等级编号为"' + ids + '"的数据项？').then(() => {
        return delLevel(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    }
  }
}
</script>
