/**
 * This file is part of WANTED: Bad-ou-Alyve.
 *
 * WANTED: Bad-ou-Alyve is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WANTED: Bad-ou-Alyve is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WANTED: Bad-ou-Alyve.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.badoualy.badoualyve.model;

import java.util.ArrayList;

public class HomeMessage {

    public String message;
    public ArrayList<Operation> operationList = new ArrayList<>();

    public HomeMessage(String message) {
        this.message = message;
    }

    public void addOperation(String url, String description) {
        operationList.add(new Operation(url, description));
    }

    public static class Operation {
        String url;
        String description;

        public Operation(String url, String description) {
            this.url = url;
            this.description = description;
        }
    }
}
