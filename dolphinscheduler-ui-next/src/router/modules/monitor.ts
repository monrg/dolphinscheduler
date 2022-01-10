/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import type { Component } from 'vue'
import utils from '@/utils'

// All TSX files under the views folder automatically generate mapping relationship
const modules = import.meta.glob('/src/views/**/**.tsx')
const components: { [key: string]: Component } = utils.mapping(modules)

export default {
  path: '/monitor',
  name: 'monitor',
  meta: { title: 'monitor' },
  redirect: { name: 'servers-master' },
  component: () => import('@/layouts/content'),
  children: [
    {
      path: '/monitor/servers/master',
      name: 'servers-master',
      component: components['master'],
      meta: {
        title: '服务管理-Master',
      },
    },
    {
      path: '/monitor/servers/worker',
      name: 'servers-worker',
      component: components['home'],
      meta: {
        title: '服务管理-Worker',
      },
    },
    {
      path: '/monitor/servers/db',
      name: 'servers-db',
      component: components['db'],
      meta: {
        title: '服务管理-DB',
      },
    },
    {
      path: '/monitor/statistics/statistics',
      name: 'statistics-statistics',
      component: components['statistics'],
      meta: {
        title: '统计管理-Statistics',
      },
    },
  ],
}