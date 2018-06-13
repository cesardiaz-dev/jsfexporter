/*
 * #%L
 * Lapis JSF Exporter Core
 * %%
 * Copyright (C) 2013 - 2015 Lapis Software Associates
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.lapis.jsfexporter.impl;

import com.lapis.jsfexporter.api.IExportCell;
import java.util.List;

public class ExportCellImpl implements IExportCell {

    private final List<String> name;
    private final String value;
    private final int columnSpanCount;
    private final int rowSpanCount;

    public ExportCellImpl(List<String> name, String value, int columnSpanCount, int rowSpanCount) {
        this.name = name;
        this.value = value;
        this.columnSpanCount = columnSpanCount;
        this.rowSpanCount = rowSpanCount;
    }

    @Override
    public List<String> getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public int getColumnSpanCount() {
        return columnSpanCount;
    }

    @Override
    public int getRowSpanCount() {
        return rowSpanCount;
    }

}
