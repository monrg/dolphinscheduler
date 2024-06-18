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

import { reactive } from 'vue'
import * as Fields from '../fields/index'
import type { IJsonItem, INodeData, ITaskData } from '../types'

export function useDbt({
  projectCode,
  from = 0,
  readonly,
  data
}: {
  projectCode: number
  from?: number
  readonly?: boolean
  data?: ITaskData
}) {
  const model = reactive({
    taskType: 'SHELL',
    name: '',
    flag: 'YES',
    description: '',
    timeoutFlag: false,
    timeoutNotifyStrategy: ['WARN'],
    localParams: [],
    environmentCode: null,
    delayTime: 0,
    rawScript: ''
  } as INodeData)

  return {
    json: [
      Fields.useName(from),
      ...Fields.useTaskDefinition({ projectCode, from, readonly, data, model }),
      ...Fields.useDbt(model)
    ] as IJsonItem[],
    model
  }
}