# Major Trauma Management Case Study
This repository contains the Digital Twins and the configuration needed to run the Major Trauma Management case study with the [Hypermedia-based WoDT Framework](https://github.com/Web-of-Digital-Twins).

## Digital Twins involved
### [White Label Digital Twins Framework](https://wldt.github.io/)
- Mission DT
- Ambulance DT
- Rescuer DT

### [Azure Digital Twins](https://learn.microsoft.com/en-us/azure/digital-twins/overview)
  - Patient DT
  - Ongoing DT

### [Eclipse Ditto](https://eclipse.dev/ditto/)
  - Healthcare User DT

## Usage
### Prerequisites
- Make sure you have a running and active version of Docker and docker compose.
- A **running** instance of [Eclipse Ditto](https://eclipse.dev/ditto/)
- The following [Azure](https://learn.microsoft.com/en-us/azure/digital-twins/overview)-based pipeline **running** (click [here](https://github.com/Web-of-Digital-Twins/azuredt-wodt-adapter-azurefunction) for the Azure Function code):

    <img src="./imgs/prototype_adt_dt.jpg" alt="azure architecture" width="70%"/>

- *N.B. we had known issues on some versions of Windows, contact the authors if you need help*.

### Steps
1. Clone the repo
2. Create a .env file in the root directory with the following variables:
    - ``
3. Run the following command:
    ```bash
      docker-compose up
    ```

## The Major Trauma Management journey
1. 


## Port mappings
- *WoDT Platform*: 4000
- *Healthcare User DT*: 3000
- *Mission DT*: 3001
- *Ambulance DT*: 3002
- *Rescuer DT*: 3003
- *Patient DT*: 5000 (same Azure adapter, `/patient` relative path)
- *Ongoing DT*: 5000 (same Azure adapter, `/trauma` relative path)