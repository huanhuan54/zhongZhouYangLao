<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="80px">
      <el-form-item label="项目名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入项目名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目类型" prop="projectType">
        <el-select v-model="queryParams.projectType" placeholder="项目类型" clearable>
          <el-option v-for="item in projectTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="项目状态" clearable>
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
          v-hasPermi="['nursing:project:add']"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['nursing:project:edit']"
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
          v-hasPermi="['nursing:project:remove']"
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

    <el-table v-loading="loading" :data="projectList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" type="index" width="80" align="center">
        <template slot-scope="scope">
          <span>{{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="项目图片" prop="image" width="110" align="center">
        <template slot-scope="scope">
          <el-image
            v-if="scope.row.image"
            :src="formatImageUrl(scope.row.image)"
            fit="cover"
            class="project-image"
            :preview-src-list="[formatImageUrl(scope.row.image)]"
          >
            <div slot="error" class="image-slot">
              <i class="el-icon-picture-outline"></i>
            </div>
          </el-image>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="项目名称" prop="name" min-width="130" />
      <el-table-column label="项目编号" prop="projectCode" width="120" align="center" />
      <el-table-column label="项目类型" prop="projectType" width="120" align="center">
        <template slot-scope="scope">
          <el-tag size="mini" :type="projectTypeTag(scope.row.projectType)">{{ scope.row.projectType || '-' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="参考价格" prop="price" width="110" align="center">
        <template slot-scope="scope">￥{{ scope.row.price }}</template>
      </el-table-column>
      <el-table-column label="服务时长" prop="serviceDuration" width="110" align="center">
        <template slot-scope="scope">{{ scope.row.serviceDuration || 0 }}分钟</template>
      </el-table-column>
      <el-table-column label="状态" prop="status" width="100" align="center">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value="1"
            :inactive-value="0"
            @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="排序" prop="orderNo" width="90" align="center" />
      <el-table-column label="操作" align="center" width="190" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)">详情</el-button>
          <el-button
            v-hasPermi="['nursing:project:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            v-hasPermi="['nursing:project:remove']"
            size="mini"
            type="text"
            icon="el-icon-delete"
            class="danger-text"
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

    <el-dialog :title="title" :visible.sync="open" width="720px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="项目名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入项目名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目编号" prop="projectCode">
              <el-input v-model="form.projectCode" placeholder="请输入项目编号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="项目类型" prop="projectType">
              <el-select v-model="form.projectType" placeholder="请选择项目类型" style="width: 100%">
                <el-option v-for="item in projectTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序" prop="orderNo">
              <el-input-number v-model="form.orderNo" controls-position="right" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="单位" prop="unit">
              <el-input v-model="form.unit" placeholder="如：次" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="参考价格" prop="price">
              <el-input-number v-model="form.price" controls-position="right" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="服务时长" prop="serviceDuration">
              <el-input-number v-model="form.serviceDuration" controls-position="right" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="项目图片" prop="image">
          <image-upload v-model="form.image" :limit="1" />
        </el-form-item>
        <el-form-item label="护理要求" prop="nursingRequirement">
          <el-input v-model="form.nursingRequirement" type="textarea" :rows="3" placeholder="请输入护理要求" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="护理项目详情" :visible.sync="detailOpen" width="660px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="项目名称">{{ detail.name }}</el-descriptions-item>
        <el-descriptions-item label="项目编号">{{ detail.projectCode }}</el-descriptions-item>
        <el-descriptions-item label="项目类型">{{ detail.projectType }}</el-descriptions-item>
        <el-descriptions-item label="参考价格">￥{{ detail.price }}</el-descriptions-item>
        <el-descriptions-item label="服务时长">{{ detail.serviceDuration }}分钟</el-descriptions-item>
        <el-descriptions-item label="排序">{{ detail.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="护理要求" :span="2">{{ detail.nursingRequirement || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { listProject, getProject, delProject, addProject, updateProject } from '@/api/nursing/project'
import { isExternal } from '@/utils/validate'

export default {
  name: 'NursingProject',
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      projectList: [],
      title: '',
      open: false,
      detailOpen: false,
      detail: {},
      projectTypeOptions: [
        { label: '生活护理', value: '生活护理' },
        { label: '医疗护理', value: '医疗护理' },
        { label: '康复护理', value: '康复护理' }
      ],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        projectType: undefined,
        status: undefined
      },
      form: {},
      rules: {
        name: [{ required: true, message: '项目名称不能为空', trigger: 'blur' }],
        projectCode: [{ required: true, message: '项目编号不能为空', trigger: 'blur' }],
        projectType: [{ required: true, message: '项目类型不能为空', trigger: 'change' }],
        orderNo: [{ required: true, message: '排序不能为空', trigger: 'blur' }],
        unit: [{ required: true, message: '单位不能为空', trigger: 'blur' }],
        price: [{ required: true, message: '参考价格不能为空', trigger: 'blur' }],
        status: [{ required: true, message: '状态不能为空', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    formatImageUrl(image) {
      if (!image) {
        return ''
      }
      if (isExternal(image) || image.indexOf('/static/') === 0) {
        return image
      }
      return process.env.VUE_APP_BASE_API + image
    },
    projectTypeTag(type) {
      return type === '医疗护理' ? 'danger' : type === '康复护理' ? 'warning' : 'primary'
    },
    getList() {
      this.loading = true
      listProject(this.queryParams).then(response => {
        this.projectList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        id: undefined,
        name: undefined,
        projectCode: undefined,
        projectType: undefined,
        orderNo: 0,
        unit: '次',
        price: 0,
        serviceDuration: 30,
        image: undefined,
        nursingRequirement: undefined,
        status: 1,
        remark: undefined
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
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增护理项目'
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids[0]
      getProject(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改护理项目'
      })
    },
    handleDetail(row) {
      getProject(row.id).then(response => {
        this.detail = response.data
        this.detailOpen = true
      })
    },
    handleStatusChange(row) {
      const text = row.status === 1 ? '启用' : '禁用'
      this.$modal.confirm('确认要' + text + '"' + row.name + '"项目吗？').then(() => {
        return updateProject(row)
      }).then(() => {
        this.$modal.msgSuccess(text + '成功')
      }).catch(() => {
        row.status = row.status === 1 ? 0 : 1
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const request = this.form.id !== undefined ? updateProject(this.form) : addProject(this.form)
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
      this.$modal.confirm('是否确认删除护理项目编号为"' + ids + '"的数据项？').then(() => {
        return delProject(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.project-image {
  width: 52px;
  height: 52px;
  border-radius: 5px;
  background-color: #ebeef5;
  box-shadow: 0 0 5px 1px #dcdfe6;
}
.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  color: #909399;
  font-size: 24px;
}
.danger-text {
  color: #f56c6c;
}
</style>
