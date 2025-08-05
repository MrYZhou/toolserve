
<template>
  <div class="upload-container">
    <div id="uploader-container" ref="uploaderContainer"></div>
    <div class="file-list">
      <div v-for="file in fileList" :key="file.id" class="file-item">
        <div class="file-info">
          <span class="file-name">{{ file.name }}</span>
          <span class="file-size">{{ formatSize(file.size) }}</span>
        </div>
        <div class="progress-container">
          <div class="progress-bar" :style="{ width: file.progress + '%' }"></div>
          <span class="progress-text">{{ file.progress }}%</span>
        </div>
        <div class="file-status">
          <span v-if="file.status === 'uploading'">上传中</span>
          <span v-else-if="file.status === 'paused'">已暂停</span>
          <span v-else-if="file.status === 'error'">上传失败</span>
          <span v-else-if="file.status === 'complete'">上传完成</span>
          <span v-else>等待上传</span>
        </div>
        <div class="file-actions">
          <button v-if="file.status === 'waiting'" @click="startUpload(file)">开始</button>
          <button v-if="file.status === 'uploading'" @click="pauseUpload(file)">暂停</button>
          <button v-if="file.status === 'paused'" @click="resumeUpload(file)">继续</button>
          <button @click="cancelUpload(file)">取消</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue';
import WebUploader from 'webuploader';
import 'webuploader/dist/webuploader.css';

const props = defineProps({
  server: {
    type: String,
    required: true
  },
  chunkSize: {
    type: Number,
    default: 5 * 1024 * 1024 // 5MB
  },
  threads: {
    type: Number,
    default: 3
  },
  accept: {
    type: Array,
    default: () => []
  }
});

const emit = defineEmits(['upload-success', 'upload-error', 'upload-complete']);

const uploaderContainer = ref(null);
const uploader = ref(null);
const fileList = ref([]);

const formatSize = (bytes) => {
  if (bytes === 0) return '0 Bytes';
  const k = 1024;
  const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
};

const initUploader = () => {
  uploader.value = WebUploader.create({
    auto: false,
    swf: '/static/webuploader/Uploader.swf',
    server: props.server,
    pick: {
      id: uploaderContainer.value,
      label: '点击选择文件',
      multiple: true
    },
    chunked: true,
    chunkSize: props.chunkSize,
    threads: props.threads,
    fileNumLimit: 10,
    fileSingleSizeLimit: 2 * 1024 * 1024 * 1024, // 2GB
    accept: {
      title: 'Files',
      extensions: props.accept.join(','),
      mimeTypes: props.accept.map(ext => `.${ext}`).join(',')
    },
    duplicate: true,
    compress: false
  });

  uploader.value.on('fileQueued', (file) => {
    fileList.value.push({
      id: file.id,
      name: file.name,
      size: file.size,
      progress: 0,
      status: 'waiting',
      file: file
    });
  });

  uploader.value.on('uploadProgress', (file, percentage) => {
    const index = fileList.value.findIndex(f => f.id === file.id);
    if (index !== -1) {
      fileList.value[index].progress = Math.round(percentage * 100);
    }
  });

  uploader.value.on('uploadStart', (file) => {
    const index = fileList.value.findIndex(f => f.id === file.id);
    if (index !== -1) {
      fileList.value[index].status = 'uploading';
    }
  });

  uploader.value.on('uploadSuccess', (file, response) => {
    const index = fileList.value.findIndex(f => f.id === file.id);
    if (index !== -1) {
      fileList.value[index].status = 'complete';
    }
    emit('upload-success', { file, response });
  });

  uploader.value.on('uploadError', (file, reason) => {
    const index = fileList.value.findIndex(f => f.id === file.id);
    if (index !== -1) {
      fileList.value[index].status = 'error';
    }
    emit('upload-error', { file, reason });
  });

  uploader.value.on('uploadComplete', (file) => {
    emit('upload-complete', file);
  });

  uploader.value.on('error', (type) => {
    if (type === 'Q_EXCEED_NUM_LIMIT') {
      alert('文件数量超过限制');
    } else if (type === 'Q_EXCEED_SIZE_LIMIT') {
      alert('文件大小超过限制');
    } else if (type === 'Q_TYPE_DENIED') {
      alert('文件类型不允许');
    }
  });
};

const startUpload = (fileItem) => {
  uploader.value.upload(fileItem.file);
};

const pauseUpload = (fileItem) => {
  uploader.value.stop(fileItem.file);
  fileItem.status = 'paused';
};

const resumeUpload = (fileItem) => {
  uploader.value.upload(fileItem.file);
};

const cancelUpload = (fileItem) => {
  uploader.value.removeFile(fileItem.file, true);
  fileList.value = fileList.value.filter(f => f.id !== fileItem.id);
};

onMounted(() => {
  initUploader();
});

onBeforeUnmount(() => {
  if (uploader.value) {
    uploader.value.destroy();
  }
});
</script>

<style scoped>
.upload-container {
  width: 100%;
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Arial', sans-serif;
}

#uploader-container {
  height: 100px;
  border: 2px dashed #ccc;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
  cursor: pointer;
  transition: all 0.3s;
}

#uploader-container:hover {
  border-color: #409eff;
  background-color: #f5f7fa;
}

.file-list {
  margin-top: 20px;
}

.file-item {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 12px;
  margin-bottom: 10px;
  background-color: #fff;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.file-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.file-name {
  font-weight: bold;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 60%;
}

.progress-container {
  height: 20px;
  background-color: #f5f5f5;
  border-radius: 10px;
  margin-bottom: 8px;
  position: relative;
}

.progress-bar {
  height: 100%;
  border-radius: 10px;
  background-color: #67c23a;
  transition: width 0.3s;
}

.progress-text {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  font-size: 12px;
  color: #333;
}

.file-status {
  font-size: 14px;
  color: #606266;
  margin-bottom: 8px;
}

.file-actions {
  display: flex;
  gap: 8px;
}

.file-actions button {
  padding: 4px 8px;
  border: none;
  border-radius: 3px;
  cursor: pointer;
  font-size: 12px;
  transition: all 0.3s;
}

.file-actions button:nth-child(1) {
  background-color: #409eff;
  color: white;
}

.file-actions button:nth-child(2) {
  background-color: #e6a23c;
  color: white;
}

.file-actions button:nth-child(3) {
  background-color: #f56c6c;
  color: white;
}

.file-actions button:hover {
  opacity: 0.8;
}
</style>
