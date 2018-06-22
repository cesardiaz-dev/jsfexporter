/*
 * #%L
 * Lapis JSF Exporter - PrimeFaces export sources
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
package com.lapis.jsfexporter.primefaces.treetable;

import com.lapis.jsfexporter.api.FacetType;
import com.lapis.jsfexporter.api.IExportCell;
import com.lapis.jsfexporter.api.IExportType;
import com.lapis.jsfexporter.impl.ExportCellImpl;
import com.lapis.jsfexporter.impl.ExportRowImpl;
import com.lapis.jsfexporter.primefaces.util.PrimeFacesUtil;
import com.lapis.jsfexporter.spi.IExportSource;
import com.lapis.jsfexporter.util.ExportUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.primefaces.component.api.DynamicColumn;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.api.UITree;
import org.primefaces.component.columngroup.ColumnGroup;
import org.primefaces.component.row.Row;
import org.primefaces.component.treetable.TreeTable;
import org.primefaces.model.TreeNode;

public class TreeTableExportSource implements IExportSource<TreeTable, Void> {

    @Override
    public Class<TreeTable> getSourceType() {
        return TreeTable.class;
    }

    @Override
    public Void getDefaultConfigOptions() {
        return null;
    }

    @Override
    public int getColumnCount(TreeTable source, Void configOptions) {
        return getColumnsForExport(source).size();
    }

    @Override
    public void exportData(TreeTable source, Void configOptions, IExportType<?, ?, ?> exporter, FacesContext context) throws Exception {
        List<UIColumn> columns = getColumnsForExport(source);
        List<String> rowName = Arrays.asList(source.getVar());
        List<List<String>> columnNames = exportFacet(FacetType.HEADER, source, columns, exporter, context);
        exportNode(source.getValue(), null, null, source, rowName, columns, columnNames, exporter, context, 0);
        exportFacet(FacetType.FOOTER, source, columns, exporter, context);
    }

    private List<UIColumn> getColumnsForExport(TreeTable source) {
        List<UIColumn> columns = new ArrayList<>();
        for (UIColumn kid : source.getColumns()) {
            if (kid.isRendered() && kid.isExportable()) {
                columns.add(kid);
            }
        }
        return columns;
    }

    private List<List<String>> exportFacet(FacetType facetType, TreeTable source, List<UIColumn> columns, IExportType<?, ?, ?> exporter, FacesContext context) {
        List<List<String>> columnNames = new ArrayList<>();
        List<IExportCell> facetCells = new ArrayList<>();
        ColumnGroup columnGroup = source.getColumnGroup(facetType.getFacetName());

        if (columnGroup == null) {
            boolean hasFacet = false;
            for (UIColumn column : columns) {
                handleDynamicColumn(column);
                String facetText = PrimeFacesUtil.getColumnFacetText(column, facetType, context);
                if (facetText != null) {
                    hasFacet = true;
                }
                columnNames.add(Arrays.asList(facetText));
            }
            if (hasFacet) {
                for (List<String> columnName : columnNames) {
                    facetCells.add(new ExportCellImpl(Arrays.asList(facetType.getFacetName()), columnName.get(0), 1, 1));
                }
                exporter.exportRow(new ExportRowImpl(Arrays.asList(facetType.getFacetName()), null, facetType, facetCells));
            }
        } else if (columnGroup.getChildCount() > 0) {
            int rowCount = columnGroup.getChildCount();
            int colCount = 0;
            for (int i = 0; i < columnGroup.getChildren().get(0).getChildCount(); i++) {
                UIColumn column = (UIColumn) columnGroup.getChildren().get(0).getChildren().get(i);
                if (column.isRendered() && column.isExportable()) {
                    colCount += column.getColspan();
                }
            }

            for (int i = 0; i < colCount; i++) {
                List<String> columnName = new ArrayList<>();
                for (int j = 0; j < rowCount; j++) {
                    columnName.add(null);
                }
                columnNames.add(columnName);
            }

            for (int rowIndex = 0; rowIndex < columnGroup.getChildCount(); rowIndex++) {
                Row row = (Row) columnGroup.getChildren().get(rowIndex);
                int columnIndex = 0;

                for (UIComponent rowChild : row.getChildren()) {
                    UIColumn rowColumn = (UIColumn) rowChild;
                    if (!rowColumn.isRendered() || !rowColumn.isExportable()) {
                        continue;
                    }
                    String facetText = PrimeFacesUtil.getColumnFacetText(rowColumn, facetType, context);

                    while (columnNames.get(columnIndex).get(rowIndex) != null) {
                        columnIndex++;
                    }

                    for (int i = 0; i < rowColumn.getColspan(); i++) {
                        for (int j = 0; j < rowColumn.getRowspan(); j++) {
                            columnNames.get(columnIndex).set(rowIndex + j, facetText);
                        }
                        columnIndex++;
                    }

                    facetCells.add(new ExportCellImpl(Arrays.asList(facetType.getFacetName()), facetText, rowColumn.getColspan(), rowColumn.getRowspan()));
                }

                exporter.exportRow(new ExportRowImpl(Arrays.asList(facetType.getFacetName()), null, facetType, facetCells));
                facetCells.clear();
            }
        }

        for (int i = 0; i < columnNames.size(); i++) {
            columnNames.set(i, new ArrayList<>(new LinkedHashSet<>(columnNames.get(i))));
        }

        return columnNames;
    }

    private UIColumn handleDynamicColumn(UIColumn column) {
        if (column instanceof DynamicColumn) {
            ((DynamicColumn) column).applyStatelessModel();
        }
        return column;
    }

    private void exportNode(TreeNode node, String rowKey, Object parentRowId, TreeTable source, List<String> rowName, List<UIColumn> columns,
            List<List<String>> columnNames, IExportType<?, ?, ?> exporter, FacesContext context, int level) {
        if (rowKey != null) {
            source.setRowKey(rowKey);

            String padding = "";
            for (int i = 0; i < level - 1; i++) {
                padding += "    ";
            }

            List<IExportCell> cells = new ArrayList<>();
            int columnCount = columns.size();
            for (int i = 0; i < columnCount; i++) {
                cells.add(new ExportCellImpl(columnNames.get(i),
                        (i == 0 ? padding : "") + ExportUtil.transformComponentsToString(context, columns.get(i).getChildren()),
                        1, 1));
            }

            parentRowId = exporter.exportRow(new ExportRowImpl(rowName, parentRowId, null, cells));
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            String childRowKey = rowKey == null ? String.valueOf(i) : rowKey + UITree.SEPARATOR + i;
            exportNode(node.getChildren().get(i), childRowKey, parentRowId, source, rowName, columns,
                    columnNames, exporter, context, level + 1);
        }
    }

}
