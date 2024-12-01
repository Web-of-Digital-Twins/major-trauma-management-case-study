/*
 * Copyright (c) 2024. Andrea Giulianelli
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.wodt.model;

/** Class that models a Qualification. */
public class Qualification {
    private final String code;
    private final String name;

    /**
     * Default constructor.
     * @param code the qualification code.
     * @param name the name of the qualification.
     */
    public Qualification(final String code, final String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Get the qualification code.
     * @return the qualification code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Get the qualification name.
     * @return the qualification name
     */
    public String getName() {
        return this.name;
    }
}
