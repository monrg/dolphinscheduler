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
import { computed, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { useResources } from '.'
import type { IJsonItem } from '../types'

export function useDatafaker(model: { [field: string]: any }): IJsonItem[] {
  const { t } = useI18n()

  const resourcesRequired = ref(
    model.programType === 'SQL' && model.sqlExecutionType === 'FILE'
  )

  const resourcesLimit = computed(() =>
    model.programType === 'SQL' && model.sqlExecutionType === 'FILE' ? 1 : -1
  )

  return [
    {
      type: 'input',
      field: 'configContent',
      span: 60,
      name: t('project.node.config_content'),
      props: {
        type: 'textarea',
        placeholder: t('project.node.config_content_tips')
      }
    },
    {
      type: 'input',
      field: 'dataNumber',
      span: 40,
      name: t('project.node.data_number'),
      props: {
        type: 'number',
        placeholder: t('project.node.data_number_tips')
      }
    },
    useResources(24, resourcesRequired, resourcesLimit)
  ]
}
