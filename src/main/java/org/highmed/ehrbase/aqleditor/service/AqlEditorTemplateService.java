/*
 *
 * Copyright (c) 2020  Stefan Spiska (Vitasystems GmbH) and Hannover Medical School
 * This file is part of Project EHRbase
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.highmed.ehrbase.aqleditor.service;

import lombok.AllArgsConstructor;
import org.highmed.ehrbase.aqleditor.dto.template.TemplateDto;
import org.ehrbase.openehr.sdk.client.openehrclient.defaultrestclient.DefaultRestClient;
import org.ehrbase.openehr.sdk.response.dto.TemplatesResponseData;
import org.ehrbase.openehr.sdk.webtemplate.filter.Filter;
import org.ehrbase.openehr.sdk.webtemplate.model.WebTemplate;
import org.ehrbase.openehr.sdk.webtemplate.parser.OPTParser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AqlEditorTemplateService {

  private final DefaultRestClient restClient;

  public List<TemplateDto> getAll() {
    TemplatesResponseData responseData = restClient.templateEndpoint().findAllTemplates();
    return responseData.get().stream()
        .map(templateMetaDataDto -> TemplateDto.builder()
            .templateId(templateMetaDataDto.getTemplateId())
            .description(templateMetaDataDto.getConcept()).build())
        .collect(Collectors.toList());
  }

  public WebTemplate getWebTemplate(String templateId) {
    return restClient.templateEndpoint().findTemplate(templateId)
        .map(o -> new OPTParser(o).parse())
        .map(w -> new Filter().filter(w))
        .orElse(null);
  }

}
