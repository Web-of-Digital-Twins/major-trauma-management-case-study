fun getCoding(element: String, code: String, display: String): RdfPredicateObjectPair =
    RdfPredicateObjectPair(
        RdfProperty(uri("http://www.hl7.org/fhir/code")),
        RdfBlankNode("$element-code", listOf(
            RdfPredicateObjectPair(
                RdfProperty(uri("http://www.hl7.org/fhir/coding")),
                RdfBlankNode("$element-coding", listOf(
                    RdfPredicateObjectPair(
                        RdfProperty(uri("http://www.hl7.org/fhir/system")),
                        RdfBlankNode("$element-coding-system", listOf(
                            RdfPredicateObjectPair(
                                RdfProperty(uri("http://www.hl7.org/fhir/v")),
                                RdfUriResource(uri("http://loinc.org"))
                            )
                        ))
                    ),
                    RdfPredicateObjectPair(
                        RdfProperty(uri("http://www.hl7.org/fhir/code")),
                        RdfBlankNode("$element-coding-code", listOf(
                            RdfPredicateObjectPair(
                                RdfProperty(uri("http://www.hl7.org/fhir/v")),
                                RdfLiteral(code)
                            )
                        ))
                    ),
                    RdfPredicateObjectPair(
                        RdfProperty(uri("http://www.hl7.org/fhir/display")),
                        RdfBlankNode("$element-coding-display", listOf(
                            RdfPredicateObjectPair(
                                RdfProperty(uri("http://www.hl7.org/fhir/v")),
                                RdfLiteral(display)
                            )
                        ))
                    ),
                ))
            )
        )),
    )

fun getValue(element: String, value: Int, unit: String, unitCode: String) =
    RdfPredicateObjectPair(
        RdfProperty(uri("http://www.hl7.org/fhir/value")),
        RdfBlankNode("$element-value", listOf(
            RdfPredicateObjectPair(
                RdfProperty(uri("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")),
                RdfClass(uri("http://www.hl7.org/fhir/Quantity"))
            ),
            RdfPredicateObjectPair(
                RdfProperty(uri("http://www.hl7.org/fhir/value")),
                RdfBlankNode("$element-value-value", listOf(
                    RdfPredicateObjectPair(
                        RdfProperty(uri("http://www.hl7.org/fhir/v")),
                        RdfLiteral(value)
                    )
                ))
            ),
            RdfPredicateObjectPair(
                RdfProperty(uri("http://www.hl7.org/fhir/unit")),
                RdfBlankNode("$element-value-unit", listOf(
                    RdfPredicateObjectPair(
                        RdfProperty(uri("http://www.hl7.org/fhir/v")),
                        RdfLiteral(unit)
                    )
                ))
            ),
            RdfPredicateObjectPair(
                RdfProperty(uri("http://www.hl7.org/fhir/system")),
                RdfBlankNode("$element-value-system", listOf(
                    RdfPredicateObjectPair(
                        RdfProperty(uri("http://www.hl7.org/fhir/v")),
                        RdfUriResource(uri("http://unitsofmeasure.org"))
                    )
                ))
            ),
            RdfPredicateObjectPair(
                RdfProperty(uri("http://www.hl7.org/fhir/code")),
                RdfBlankNode("$element-value-code", listOf(
                    RdfPredicateObjectPair(
                        RdfProperty(uri("http://www.hl7.org/fhir/v")),
                        RdfLiteral(unitCode)
                    )
                ))
            ),
        ))
    )

configuration {
    dt("patient") {
        relativeUri = uri("patient")
        version = DTVersion(1, 0, 0)
        physicalAssetID = "patientPA"
        platformsToRegister = listOf(uri("http://localhost:4000"))
        semantics {
            classes {
                -"http://www.hl7.org/fhir/Observation"
            }
            domainTags {
                -("heart_rate" to uri("http://loinc.org/8867-4"))
                -("diastolic_blood_pressure" to uri("http://loinc.org/8462-4"))
                -("systolic_blood_pressure" to uri("http://loinc.org/8480-6"))
                -("rel_of_ongoing_trauma" to uri("http://ontology.com/partOf"))
                -("rel_refer_to_healthcare_user" to uri("http://www.hl7.org/fhir/subject"))
            }
            propertyMapping {
                -(
                    "heart_rate" to { value: Any ->
                        listOf(
                            getCoding("", code = "8716-3", display = "Combined vital signs"),
                            RdfPredicateObjectPair(
                                RdfProperty(uri("http://www.hl7.org/fhir/component")),
                                RdfBlankNode("heart_rate-component", listOf(
                                    getCoding("heart_rate", code = "8867-4", display = "Heart rate"),
                                    getValue("heart_rate", value = value.toString().toDouble().toInt(), unit = "beats/minute", unitCode = "/min")
                                ))
                            ),
                        )
                    }
                    )
                -(
                    "diastolic_blood_pressure" to { value: Any ->
                        listOf(
                            RdfPredicateObjectPair(
                                RdfProperty(uri("http://www.hl7.org/fhir/component")),
                                RdfBlankNode("diastolic_blood_pressure-component", listOf(
                                    getCoding("diastolic_blood_pressure", code = "8462-4", display = "Diastolic blood pressure"),
                                    getValue("diastolic_blood_pressure", value = value.toString().toDouble().toInt(), unit = "mmHg", unitCode = "mm[Hg]")
                                ))
                            ),
                        )
                    }
                    )
                -(
                    "systolic_blood_pressure" to { value: Any ->
                        listOf(
                            RdfPredicateObjectPair(
                                RdfProperty(uri("http://www.hl7.org/fhir/component")),
                                RdfBlankNode("systolic_blood_pressure-component", listOf(
                                    getCoding("systolic_blood_pressure", code = "8480-6", display = "Systolic blood pressure"),
                                    getValue("systolic_blood_pressure", value = value.toString().toDouble().toInt(), unit = "mmHg", unitCode = "mm[Hg]")
                                ))
                            ),
                        )
                    }
                    )
            }
            relationshipMapping {
                -(
                    "rel_of_ongoing_trauma" to { target: URI ->
                        listOf(
                            RdfPredicateObjectPair(
                                RdfProperty(uri("http://ontology.com/partOf")),
                                RdfIndividual(uri(target.toString())),
                            ),
                        )
                    }
                    )
                -(
                    "rel_refer_to_healthcare_user" to { target: URI ->
                        listOf(
                            RdfPredicateObjectPair(
                                RdfProperty(uri("http://www.hl7.org/fhir/subject")),
                                RdfIndividual(uri(target.toString())),
                            ),
                        )
                    }
                    )
            }
        }
    }

    dt("trauma") {
        relativeUri = uri("trauma")
        version = DTVersion(1, 0, 0)
        physicalAssetID = "traumaPA"
        platformsToRegister = listOf(uri("http://localhost:4000"))
        semantics {
            classes {
                -"http://ontology.com/OngoingTrauma"
            }
            domainTags {
                -("status" to uri("http://ontology.com/status"))
            }
            propertyMapping {
                -(
                        "status" to { value: Any ->
                            listOf(
                                RdfPredicateObjectPair(
                                    RdfProperty(uri("http://ontology.com/status")),
                                    RdfLiteral(value.toString())
                                ),
                            )
                        }
                        )

            }
            relationshipMapping { }
        }
    }
}