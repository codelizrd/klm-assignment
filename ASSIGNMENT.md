# Assignment

*AAFE Airways* (acronym for: An Airline that Flies Everywhere) is a brand-new airline that wants to stand out by offering a great network with prices that are always the same, despite when or how you book.

In this assignment you will be building a web-application that calculates the ticket price given an itinerary. 

An itinerary is a planned route over two or more stations. For this assignment you can assume that a direct connection always exists between any of the allowed stations.

The list of stations AAFE Airways currently flies on is (denoted by their IATA code):
```
AAL,AAR,ABZ,ACC,ACE,AES,AGP,ALC,AMM,AMS,ARN,ATH,ATL,AUA,AUH,AUS,BAH,BCN,BEG,BER,BEY,BGI,BGO,BHD,BHX,BIO,BKK,BLL,BLQ,BLR,BOD,BOG,BOJ,BOM,BON,BOS,BRE,BRI,BRS,BRU,BSL,BUD,CAG,CAN,CDG,CFU,CGK,CHQ,CMN,CPH,CPT,CTA,CTG,CUN,CUR,CWL,DAR,DBV,DEL,DMM,DPS,DRS,DTW,DUB,DUS,DXB,EBB,EDI,EFL,EZE,FAO,FCO,FLR,FNC,FRA,FUE,GDN,GIG,GLA,GOA,GOT,GRO,GRU,GRZ,GUA,GVA,GYE,HAJ,HAM,HEL,HER,HKG,HRE,HRG,HUY,IAD,IAH,IBZ,ICN,INN,INV,IST,JFK,JMK,JNB,JRO,JTR,KBP,KEF,KGL,KGS,KIX,KLX,KRK,KRS,KTW,KUL,KWI,LAS,LAX,LBA,LCA,LCY,LED,LHR,LIM,LIN,LIR,LIS,LJU,LOS,LPA,LPI,LUX,LYS,MAD,MAH,MAN,MCO,MCT,MEX,MIA,MLA,MME,MNL,MPL,MRS,MSP,MUC,MXP,NAP,NBO,NCE,NCL,NRT,NTE,NUE,NWI,OLB,OPO,ORD,ORK,ORY,OSL,OTP,PBM,PDX,PEK,PFO,PKX,PMI,POS,POZ,PRG,PSA,PTY,PVG,PVK,RAK,RHO,RIX,RNS,RUH,RVN,SCL,SEA,SFO,SIN,SJO,SKG,SLC,SMI,SOU,SPC,SPU,SSH,STR,SVG,SVO,SVQ,SXM,SZG,TBS,TFS,TLS,TLV,TOS,TPE,TRD,TRF,TRN,UIO,VCE,VCP,VIE,VLC,VRN,WAW,WRO,XMN,YEG,YUL,YVR,YYC,YYZ,ZAG,ZNZ,ZRH,ZTH
```

The total ticket total fare for the itinerary depends on the total amount of kilometers flown in the whole itinerary (calculated using the Haversine formula), and the number of stations involved:

> Ticket price = 0.05 * Total number of km + 25.00 * total number of intermediate stations + 50.00

As AAFE Airways is based in the Netherlands, the VAT rate is 21%.

The itinerary can be entered in the URL directly by the user using the IATA station codes:

 - http://localhost:8001/itinerary/AMS/LAX
 - http://localhost:8001/itinerary/AMS/BCN/LAX
 - http://localhost:8001/itinerary/AMS/BCN/GOT/LAX

The first URL will calculate the total fare for departure in Amsterdam and arrival in Los Angeles
The second URL will do the same, but will have stop in Barcelona in between, etc.

You only have to manage the itinerary and calculate the total ticket fare. Information about airports is retrieved from the 'Airport Service', see below.


## Project setup

1. Create a **private** repository in GitHub.
2. Clone this repository as a bare repository: ```git clone --bare https://github.com/codelizrd/klm-assignment.git```
3. Go into the directory: ```cd klm-assignment.git```
4. Set your private repository: ```git push --mirror https://github.com/yourname/your-private-repo.git```
5. Remove the directory: ```cd .. && rm -rf klm-assignment.git```
6. Clone your private repository: ```git clone https://github.com/yourname/your-private-repo.git```

Start your project in the 'solution' directory using [Spring Initializr](https://start.spring.iomarkup)

For this project choose the build tool (Maven or Gradle), language (Java or Kotlin) and libraries you are most proficient in.

Use GIT best practices to regularly commit completed features.


## Delivery
Make sure your project compiles without warnings or errors. Include a README-file with a short explanation on your solution, how much time you spent on it and how to run. Then add these collaborators to your private repository, unless stated otherwise by the person sending you the assignment:
 - codelizrd
 - goran


## Airports Service

The Airport Service is a microservice exposing all the airports on the world. You can start it using:

```sh
cd server
./gradlew bootRun
```

The API provided by the Airports Service can be tested using:

http://localhost:8000/webjars/swagger-ui/index.html

You can also download the OpenApi specification using:

http://localhost:8000/api.yaml


## Show your skills!

As you might have noticed, providing the input for the itinerary using the URL is quite cumbersome and the Airports Service requests sometimes take a while. Additionally, airline industry regulations forbid itinerary to start or end in certain countries and some stations charge higher fees which should be reflected in the ticket price.

The product owner created a backlog of additional User Stories to be implemented. Impress the product owner by including one or more of these features; it’s up to you to show your skills and collect bonus points!


### Fare explanation

In order to validate correctness and aid customer service support the application has to log how the total ticket fare is calculated. As this information is very competition sensitive customers should not be able to see this decomposition.

### User interface

Instead of having the user lookup the station codes for the itinerary directly in the URL, create a user interface in which the user can search for stations and add it to the list.


### Adaptability

Stations are added or removed on a regular basis and as the airlines market is very competitive prices change regularly.

Provide a way to make it easier to allow for these frequent changes.


### Not allowed itineraries

Due to current regulations, itineraries cannot start in these stations:
BHD,LCA,HAJ,ACE,TPE,FLR,ATL,MUC

Due to current regulations, itineraries cannot end in these stations:
CAG,LYS,ATH,GYE,SMI,OLB

If such an itinerary is entered the user must be given an explanation.


### Service charges

Not every station charges the same fee for handling a passenger. Depending on the role of the airport certain countries have an additional fee which must be added to the total ticket price:

If the itinerary starts in certain stations an additional fee must be added:
```
SIN,CFU,AMM,LIR,BOD,GDN,AMS,BRE:                                +50.00 
IAH,PKX,LED,AAL:                                                +30.00 
SPC,SZG,SXM,ZAG,DAR,TBS:                                        +20.00 
```

If the itinerary ends in certain stations a additional fee must be added:
```
CMN,OTP,TBS,SMI,BRI,IAD:                                        +50.00
LIM,GLA,MAH,DAR,DUS,BOS,NCL,BRS,PSA,ZRH,EFL,FCO,AUS,ACC,SKG:    +30.00 
PMI,KLX,SFO,ATL,BSL,ALC,BLL,PRG,CTA,NCE,NUE,DPS,LPA,GYE,OSL:    +20.00 
```

If the itinerary passes through certain stations an additional fee must be added:
```
SOU,DBV,CTA,CAG,KRK,LHR,BCN,HAM,INV,IBZ,GDN,KTW,HRE,VCE,ABZ     +35.00
SZG,HAJ,CHQ,ORY,ACC,BKK,BHD:                                    +25.00
WRO,RIX,TRF,BOG,ACE,PRG,KEF,MCO,BOJ,NBO,LIR,BGO,LBA:            +15.00
```


### Performance

Optimize your application in such a way to provide the best possible user experience, ***without*** changing the Airport service and without fetching data preemptive. Keep in mind that every call to the Airport Service costs AAFE airlines money.


### Request correlation

In a microservice architecture it can be difficult to troubleshoot problems. Take a look at the API of the Airport Service and use existing functionality to trace requests through the whole system. 

