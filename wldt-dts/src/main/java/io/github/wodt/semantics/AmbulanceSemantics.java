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
import io.github.wodt.model.Position;
import it.wldt.core.state.DigitalTwinStateAction;
import it.wldt.core.state.DigitalTwinStateProperty;
import it.wldt.core.state.DigitalTwinStateRelationship;
import it.wldt.core.state.DigitalTwinStateRelationshipInstance;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Ambulance Semantics. */
public class AmbulanceSemantics implements DigitalTwinSemantics {
    private static final Map<String, RdfUriResource> PROPERTIES_DOMAIN_TAG = Map.of(
            "status", new RdfUriResource(URI.create("http://www.hl7.org/fhir/status")),
            "position", new RdfUriResource(URI.create("http://www.hl7.org/fhir/position"))
    );

    private static final Map<String, RdfUriResource> ACTIONS_DOMAIN_TAG = Map.of(
            "setDestination", new RdfUriResource(URI.create("https://purl.org/onto/SetDestinationCommand"))
    );

    @Override
    public List<RdfClass> getDigitalTwinTypes() {
        return List.of(new RdfClass(URI.create("http://www.hl7.org/fhir/Location")));
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
        return getOptionalFromMap(ACTIONS_DOMAIN_TAG, digitalTwinStateAction.getKey());
    }

    @Override
    public Optional<List<RdfUnSubjectedTriple>> mapData(DigitalTwinStateProperty<?> digitalTwinStateProperty) {
        if ("status".equals(digitalTwinStateProperty.getKey())) {
            return Optional.of(List.of(
                    new RdfUnSubjectedTriple(
                        new RdfProperty(URI.create("http://www.hl7.org/fhir/status")),
                        new RdfLiteral<>(digitalTwinStateProperty.getValue().toString())
                    )
                )
            );
        } else if ("position".equals(digitalTwinStateProperty.getKey())) {
            return Optional.of(List.of(
                    new RdfUnSubjectedTriple(
                        new RdfProperty(URI.create("http://www.hl7.org/fhir/position")),
                        new RdfBlankNode("ambulancePosition", List.of(
                                new RdfUnSubjectedTriple(
                                    new RdfProperty(URI.create("http://www.hl7.org/fhir/latitude")),
                                    new RdfBlankNode("ambulancePositionLatitude", List.of(
                                        new RdfUnSubjectedTriple(
                                            new RdfProperty(URI.create("http://www.hl7.org/fhir/v")),
                                            new RdfLiteral<>(((Position) digitalTwinStateProperty.getValue()).getLatitude())
                                        )
                                    ))
                                ),
                                new RdfUnSubjectedTriple(
                                    new RdfProperty(URI.create("http://www.hl7.org/fhir/longitude")),
                                    new RdfBlankNode("ambulancePositionLongitude", List.of(
                                        new RdfUnSubjectedTriple(
                                            new RdfProperty(URI.create("http://www.hl7.org/fhir/v")),
                                            new RdfLiteral<>(((Position) digitalTwinStateProperty.getValue()).getLongitude())
                                        )
                                    ))
                                )
                        ))
                    )
                )
            );
        }

        return Optional.empty();
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
