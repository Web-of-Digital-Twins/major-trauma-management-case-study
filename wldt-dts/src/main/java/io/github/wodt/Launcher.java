/*
 * Copyright (c) 2023. Andrea Giulianelli
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

package io.github.wodt;

import io.github.webbasedwodt.adapter.WoDTDigitalAdapter;
import io.github.webbasedwodt.adapter.WoDTDigitalAdapterConfiguration;
import io.github.webbasedwodt.model.dtd.DTVersion;
import io.github.wodt.physicaladapter.AmbulancePhysicalAdapter;
import io.github.wodt.physicaladapter.MissionPhysicalAdapter;
import io.github.wodt.physicaladapter.RescuerPhysicalAdapter;
import io.github.wodt.semantics.AmbulanceSemantics;
import io.github.wodt.semantics.MissionSemantics;
import io.github.wodt.semantics.RescuerSemantics;
import io.github.wodt.shadowing.MirrorShadowingFunction;
import it.wldt.core.engine.DigitalTwin;

import java.net.URI;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

import it.wldt.core.engine.DigitalTwinEngine;
import it.wldt.exception.EventBusException;
import it.wldt.exception.ModelException;
import it.wldt.exception.WldtConfigurationException;
import it.wldt.exception.WldtDigitalTwinStateException;
import it.wldt.exception.WldtEngineException;
import it.wldt.exception.WldtRuntimeException;
import it.wldt.exception.WldtWorkerException;

/**
 * Java template project.
 */
public final class Launcher {
    private static final String MISSION_EXPOSED_PORT_VARIABLE = "MISSION_EXPOSED_PORT";
    private static final String AMBULANCE_EXPOSED_PORT_VARIABLE = "AMBULANCE_EXPOSED_PORT";
    private static final String RESCUER_EXPOSED_PORT_VARIABLE = "RESCUER_EXPOSED_PORT";
    private static final String MISSION_PLATFORM_URL_VARIABLE = "MISSION_PLATFORM_URL";
    private static final String PATIENT_URI_VARIABLE = "PATIENT_URI";

    static {
        // Checks on existence of environmental variables
        Objects.requireNonNull(System.getenv(MISSION_EXPOSED_PORT_VARIABLE), "Please provide the Mission DT exposed port");
        Objects.requireNonNull(System.getenv(AMBULANCE_EXPOSED_PORT_VARIABLE), "Please provide the Ambulance DT exposed port");
        Objects.requireNonNull(System.getenv(RESCUER_EXPOSED_PORT_VARIABLE), "Please provide the Rescuer DT exposed port");
        Objects.requireNonNull(System.getenv(MISSION_PLATFORM_URL_VARIABLE), "Please provide the platform url");
        Objects.requireNonNull(System.getenv(PATIENT_URI_VARIABLE), "Please provide the Patient DT uri");
    }

    private Launcher() { }

    /**
     * Main function.
     * @param args
     */
    public static void main(final String[] args) {
        try {
            final int missionPortNumber = Integer.parseInt(System.getenv(MISSION_EXPOSED_PORT_VARIABLE));
            final int ambulancePortNumber = Integer.parseInt(System.getenv(AMBULANCE_EXPOSED_PORT_VARIABLE));
            final int rescuerPortNumber = Integer.parseInt(System.getenv(RESCUER_EXPOSED_PORT_VARIABLE));

            final String missionDTId = "mission-dt";
            final DigitalTwin missionDT = new DigitalTwin(missionDTId, new MirrorShadowingFunction("mission-shadowing-function"));
            missionDT.addPhysicalAdapter(new MissionPhysicalAdapter(
                    "http://localhost:" + ambulancePortNumber,
                    "http://localhost:" + rescuerPortNumber,
                    System.getenv(PATIENT_URI_VARIABLE)
            ));
            missionDT.addDigitalAdapter(new WoDTDigitalAdapter(
                    "mission-dt-adapter",
                    new WoDTDigitalAdapterConfiguration(
                            URI.create("http://localhost:" + missionPortNumber),
                            new DTVersion(1, 0, 0),
                            new MissionSemantics(),
                            missionPortNumber,
                            "missionPA",
                            Set.of(URI.create(System.getenv(MISSION_PLATFORM_URL_VARIABLE)))
                    )
            ));

            final String ambulanceDTId = "ambulance-dt";
            final DigitalTwin ambulanceDT = new DigitalTwin(ambulanceDTId, new MirrorShadowingFunction("ambulance-shadowing-function"));
            ambulanceDT.addPhysicalAdapter(new AmbulancePhysicalAdapter());
            ambulanceDT.addDigitalAdapter(new WoDTDigitalAdapter(
                    "ambulance-dt-adapter",
                    new WoDTDigitalAdapterConfiguration(
                            URI.create("http://localhost:" + ambulancePortNumber),
                            new DTVersion(1, 0, 0),
                            new AmbulanceSemantics(),
                            ambulancePortNumber,
                            "AMBULANCE010203",
                            Set.of()
                    )
            ));

            final String rescuerDTId = "rescuer-dt";
            final DigitalTwin rescuerDT = new DigitalTwin(rescuerDTId, new MirrorShadowingFunction("rescuer-shadowing-function"));
            rescuerDT.addPhysicalAdapter(new RescuerPhysicalAdapter());
            rescuerDT.addDigitalAdapter(new WoDTDigitalAdapter(
                    "rescuer-dt-adapter",
                    new WoDTDigitalAdapterConfiguration(
                            URI.create("http://localhost:" + rescuerPortNumber),
                            new DTVersion(1, 0, 0),
                            new RescuerSemantics(),
                            rescuerPortNumber,
                            "ABC123",
                            Set.of()
                    )
            ));

            final DigitalTwinEngine digitalTwinEngine = new DigitalTwinEngine();
            digitalTwinEngine.addDigitalTwin(missionDT);
            digitalTwinEngine.addDigitalTwin(ambulanceDT);
            digitalTwinEngine.addDigitalTwin(rescuerDT);

            digitalTwinEngine.startDigitalTwin(missionDTId);
            digitalTwinEngine.startDigitalTwin(ambulanceDTId);
            digitalTwinEngine.startDigitalTwin(rescuerDTId);
        } catch (ModelException
                 | WldtDigitalTwinStateException
                 | WldtWorkerException
                 | WldtRuntimeException
                 | EventBusException
                 | WldtConfigurationException
                 | WldtEngineException e) {
            Logger.getLogger(Launcher.class.getName()).info(e.getMessage());
        }
    }
}
