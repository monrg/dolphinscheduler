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
import { computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { useResources } from '.'
import type { IJsonItem } from '../types'

export function useDbt(model: { [field: string]: any }): IJsonItem[] {
  const { t } = useI18n()
  const mainArgsSpan = computed(() => (model.programType = 40))
  return [
    {
      type: 'input',
      field: 'modelSchemaContent',
      span: mainArgsSpan,
      name: t('project.node.model_schema_content'),
      props: {
        type: 'textarea',
        placeholder: t('project.node.model_schema_content_tips')
      }
    },
    {
      type: 'input',
      field: 'modelContent',
      span: mainArgsSpan,
      name: t('project.node.model_content'),
      props: {
        type: 'textarea',
        placeholder: t('project.node.model_content_tips')
      }
    },
    useResources(24, true, 1)
  ]
}
