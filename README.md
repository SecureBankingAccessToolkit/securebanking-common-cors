# Description
Securebanking common cors repository consist of library that contains a collection of classes or interfaces which are used by many portions of the Secure Banking Access Tool Kit (SBAT).

This library include functionalities used by the SBAT and is not related with a specific implementation of Open Banking specification.

> Cross-origin resource sharing (CORS) is a standard mechanism that allows JavaScript XMLHttpRequest (XHR) calls executed in a web page to interact with resources from non-origin domains. CORS is a commonly implemented solution to the same-origin policy that is enforced by all browsers.

## library purpose
Collection of classes, interfaces, utils etc... to supply mechanisms to comply with CORS policies, for example a filter class to build properly the headers expected by the browser when a resource is served from a different domain it has been requested.

### Properties library configuration map
| key                 | description                                    | default value |
|---------------------|------------------------------------------------|---------------|
| allowed_origins     | CORS Domains allowed (list)                    | localhost     |
| allowed_headers     | Headers can be used (string)                   |               |
| allowed_methods     | Allowed methods for preflight request (string) |               |
| allowed_credentials | Credentials mode accepted (boolean)            | true          |
| max_age             | Expiration time of preflight request (string)  | 3600          |

> For test purposes the CORS filter supports the value `"*"` to allow any origin

All `securebanking-common-*` libraries use the configuration root key `common` to add under it his own properties structure.

Example:
````yaml
common: # root key for all common 'securebanking-common-*' libraries
  cors: # library name
    allowed_origins:
      - "*" # optional value to allow any origin domain
      - localhost
      - forgerock.financial
      - domain4test.com # don't delete it!
    allowed_headers: accept-api-version, x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN, Id-Token
    allowed_methods: GET, PUT, POST, DELETE, OPTIONS, PATCH
    allowed_credentials: true # default value true
    max_age: 3600 # default value 3600
  otherCommonLibrary:
    other_key: other_value
    ....
````
### Components
#### CorsFilter
This filter on server-side performs filtering tasks to validate the multiple domains allowed for non-origin domain requests to comply with CORS policy.

#### CorsConfigurationProperties
To load the external properties with prefix `common.cors` from file and map them as properties object.
See [Properties map](#properties-library-configuration-map)

## Usage
Import into your maven dependencies sections like this (use the latest released version);

```
   <dependencyManagement>
        <dependencies>
            <!-- Secure Banking: Open Banking UK common dependencies -->
            <dependency>
                <groupId>com.forgerock.securebanking.common</groupId>
                <artifactId>securebanking-common-cors</artifactId>
                <version>1.0.0-SNAPSHOT</version>
            </dependency>
            ...
        </dependencies>
    </dependencyManagement>
```

### How to Build

This is a Java Maven project. 

#### Prerequisites
You need the following on your development machine;
- Java 14 or later
- Maven 3.6.0 or later

#### Building

```shell
git checkout git@github.com:SecureBankingAcceleratorToolkit/securebanking-common-cors.git
cd securebanking-common-cors
mvn clean install
```


## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License 
Released under an Apache 2.0 license
