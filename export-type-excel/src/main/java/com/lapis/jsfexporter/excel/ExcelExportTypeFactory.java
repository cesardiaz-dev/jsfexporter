/*
 * #%L
 * Lapis JSF Exporter - Excel export type
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
package com.lapis.jsfexporter.excel;

import com.lapis.jsfexporter.api.IExportType;
import com.lapis.jsfexporter.spi.IExportTypeFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelExportTypeFactory implements IExportTypeFactory<Workbook, ExcelExportOptions, Row> {

    @Override
    public IExportType<Workbook, ExcelExportOptions, Row> createNewExporter(ExcelExportOptions configOptions) {
        return new ExcelExportType(configOptions);
    }

    @Override
    public ExcelExportOptions getDefaultConfigOptions() {
        return new ExcelExportOptions();
    }

    @Override
    public String getExportTypeId() {
        return "excel";
    }

}
