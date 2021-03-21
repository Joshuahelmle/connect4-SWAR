FROM hseeberger/scala-sbt:8u181_2.12.7_1.2.6
WORKDIR /connect4
ADD . /connect4
CMD sbt run