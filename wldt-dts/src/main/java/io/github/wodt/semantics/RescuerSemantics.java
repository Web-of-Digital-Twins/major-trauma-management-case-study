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

package io.github.wodt.semantics;

import io.github.webbasedwodt.model.ontology.DigitalTwinSemantics;
import io.github.webbasedwodt.model.ontology.rdf.RdfBlankNode;
import io.github.webbasedwodt.model.ontology.rdf.RdfClass;
import io.github.webbasedwodt.model.ontology.rdf.RdfLiteral;
import io.github.webbasedwodt.model.ontology.rdf.RdfProperty;
import io.github.webbasedwodt.model.ontology.rdf.RdfUnSubjectedTriple;
import io.github.webbasedwodt.model.ontology.rdf.RdfUriResource;
import io.github.wodt.model.NameSurname;
import io.github.wodt.model.Qualification;
import it.wldt.core.state.DigitalTwinStateAction;
import it.wldt.core.state.DigitalTwinStateProperty;
import it.wldt.core.state.DigitalTwinStateRelationship;
import it.wldt.core.state.DigitalTwinStateRelationshipInstance;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Rescuer Semantics. */
public class RescuerSemantics implements DigitalTwinSemantics {

    private static final Map<String, RdfUriResource> PROPERTIES_DOMAIN_TAG = Map.of(
            "identifier", new RdfUriResource(URI.create("http://www.hl7.org/fhir/identifier")),
            "name", new RdfUriResource(URI.create("http://www.hl7.org/fhir/HumanName")),
            "qualification", new RdfUriResource(URI.create("http://www.hl7.org/fhir/qualification"))
    );

    @Override
    public List<RdfClass> getDigitalTwinTypes() {
        return List.of(new RdfClass(URI.create("http://www.hl7.org/fhir/Practitioner")));
    }

    @Override
    public Optional<RdfUriResource> getDomainTag(DigitalTwinStateProperty<?> digitalTwinStateProperty) {
        return getOptionalFromMap(PROPERTIES_DOMAIN_TAG, digitalTwinStateProperty.getKey());
    }

    @Override
    public Optional<RdfUriResource> getDomainTag(DigitalTwinStateRelationship<?> digitalTwinStateRelationship) {
        return Optional.empty();
    }

    @Override
    public Optional<RdfUriResource> getDomainTag(DigitalTwinStateAction digitalTwinStateAction) {
        return Optional.empty();
    }

    @Override
    public Optional<List<RdfUnSubjectedTriple>> mapData(DigitalTwinStateProperty<?> digitalTwinStateProperty) {
        switch (digitalTwinStateProperty.getKey()) {
            case "identifier":
                return Optional.of(List.of(
                    new RdfUnSubjectedTriple(
                        new RdfProperty(URI.create("http://www.hl7.org/fhir/identifier")),
                        new RdfBlankNode("rescuer-identifier", List.of(
                            new RdfUnSubjectedTriple(
                                new RdfProperty(URI.create("http://www.hl7.org/fhir/value")),
                                new RdfBlankNode("rescue-identifier-value", List.of(
                                    new RdfUnSubjectedTriple(
                                        new RdfProperty(URI.create("http://www.hl7.org/fhir/v")),
                                        new RdfLiteral<>(digitalTwinStateProperty.getValue().toString())
                                    )
                                ))
                            )
                        ))
                    )
                ));
            case "name":
                return Optional.of(List.of(
                    new RdfUnSubjectedTriple(
                        new RdfProperty(URI.create("http://www.hl7.org/fhir/name")),
                            new RdfBlankNode("rescuer-name", List.of(
                                new RdfUnSubjectedTriple(
                                    new RdfProperty(URI.create("http://www.hl7.org/fhir/given")),
                                    new RdfBlankNode("rescue-identifier-name-value", List.of(
                                        new RdfUnSubjectedTriple(
                                            new RdfProperty(URI.create("http://www.hl7.org/fhir/v")),
                                            new RdfLiteral<>(((NameSurname)digitalTwinStateProperty.getValue()).getName())
                                        )
                                    ))
                                ),
                                new RdfUnSubjectedTriple(
                                    new RdfProperty(URI.create("http://www.hl7.org/fhir/family")),
                                    new RdfBlankNode("rescue-identifier-surname-value", List.of(
                                        new RdfUnSubjectedTriple(
                                            new RdfProperty(URI.create("http://www.hl7.org/fhir/v")),
                                            new RdfLiteral<>(((NameSurname)digitalTwinStateProperty.getValue()).getSurname())
                                        )
                                    ))
                                )
                            ))
                    )
                ));
            case "qualification":
                return Optional.of(List.of(
                    new RdfUnSubjectedTriple(
                        new RdfProperty(URI.create("http://www.hl7.org/fhir/qualification")),
                        new RdfBlankNode("rescuer-qualification", List.of(
                            new RdfUnSubjectedTriple(
                                new RdfProperty(URI.create("http://www.hl7.org/fhir/code")),
                                new RdfBlankNode("rescue-qualification-code", List.of(
                                    new RdfUnSubjectedTriple(
                                        new RdfProperty(URI.create("http://www.hl7.org/fhir/coding")),
                                        new RdfBlankNode("rescue-qualification-coding", List.of(
                                            new RdfUnSubjectedTriple(
                                                new RdfProperty(URI.create("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")),
                                                new RdfUriResource(URI.create("http://snomed.info/id/" + ((Qualification)digitalTwinStateProperty.getValue()).getCode()))
                                            ),
                                            new RdfUnSubjectedTriple(
                                                new RdfProperty(URI.create("http://www.hl7.org/fhir/system")),
                                                new RdfBlankNode("rescue-qualification-system", List.of(
                                                    new RdfUnSubjectedTriple(
                                                        new RdfProperty(URI.create("http://www.hl7.org/fhir/v")),
                                                        new RdfUriResource(URI.create("http://snomed.info/sct"))
                                                    )
                                                ))
                                            ),
                                            new RdfUnSubjectedTriple(
                                                new RdfProperty(URI.create("http://www.hl7.org/fhir/code")),
                                                new RdfBlankNode("rescue-qualification-coding-value", List.of(
                                                    new RdfUnSubjectedTriple(
                                                        new RdfProperty(URI.create("http://www.hl7.org/fhir/v")),
                                                        new RdfLiteral<>(((Qualification)digitalTwinStateProperty.getValue()).getCode())
                                                    )
                                                ))
                                            ),
                                            new RdfUnSubjectedTriple(
                                                new RdfProperty(URI.create("http://www.hl7.org/fhir/display")),
                                                new RdfBlankNode("rescue-qualification-coding-display", List.of(
                                                    new RdfUnSubjectedTriple(
                                                        new RdfProperty(URI.create("http://www.hl7.org/fhir/v")),
                                                        new RdfLiteral<>(((Qualification)digitalTwinStateProperty.getValue()).getName())
                                                    )
                                                ))
                                            )
                                        ))
                                    )
                                ))
                            )
                        ))
                    )
                ));
            default:
                return Optional.empty();
        }
    }

    @Override
    public Optional<List<RdfUnSubjectedTriple>> mapData(DigitalTwinStateRelationshipInstance<?> digitalTwinStateRelationshipInstance) {
        return Optional.empty();
    }

    private <T> Optional<T> getOptionalFromMap(final Map<String, T> map, final String key) {
        if (map.containsKey(key)) {
            return Optional.of(map.get(key));
        }
        return Optional.empty();
    }
}
