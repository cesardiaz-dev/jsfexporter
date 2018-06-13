/*
 * #%L
 * Lapis JSF Exporter - Test WAR
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
package com.lapis.jsfexporter.test;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class PanelGridBean implements Serializable {

    // see http://thedailywtf.com/Articles/What_Is_Truth_0x3f_.aspx
    public enum TheDailyWTFBoolean {
        TRUE("True"), FALSE("False"), FILE_NOT_FOUND("FileNotFound");

        private String label;

        private TheDailyWTFBoolean(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    private TheDailyWTFBoolean selectedChoice;

    public TheDailyWTFBoolean getSelectedChoice() {
        return selectedChoice;
    }

    public void setSelectedChoice(TheDailyWTFBoolean selectedChoice) {
        this.selectedChoice = selectedChoice;
    }

    public TheDailyWTFBoolean[] getSelectOneChoices() {
        return TheDailyWTFBoolean.values();
    }

}
