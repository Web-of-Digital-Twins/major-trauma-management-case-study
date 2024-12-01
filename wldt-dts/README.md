# Emergency call management and planning WoDT Digital Twins

This Java project contains the White Label Digital Twins implementation of the *emergency call management and planning* Digital Twins of the Major Trauma Management case study:
- **Mission DT**
- **Rescuer DT**
- **Ambulance DT**

To be compliant with the [**Hypermedia-based Web of Digital Twins**](https://github.com/Web-of-Digital-Twins), they use the [`wldt-wodt-adapter`](https://github.com/Web-of-Digital-Twins/wldt-wodt-adapter)

## Setup
To be able to run these Digital Twins, two Gradle Project Properties are needed (because the [`wldt-wodt-adapter`](https://github.com/Web-of-Digital-Twins/wldt-wodt-adapter) library is published on GitHub Packages):
- `ghPackagesUsername`: a valid GitHub username
- `ghPackagesPwd`: a valid GitHub token associated to the username

Then, you need to specify the following environment variable:
- `MISSION_EXPOSED_PORT`: the port used to expose the Mission Digital Twin
- `AMBULANCE_EXPOSED_PORT`: the port used to expose the Ambulance Digital Twin
- `RESCUER_EXPOSED_PORT`: the port used to expose the Rescuer Digital Twin
- `MISSION_PLATFORM_URL`: the platform to which the Mission Digital Twin should register at startup
- `PATIENT_URI`: the Patient DT URI of in relationship with the Mission
