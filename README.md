# NovelsAggregator
Scheduled Multi-Threaded Novels Aggregator. Specifically designed for China's on-line novels subscription market.

Online novel subscription is a multi-billions business back in China. This app able to fetch Free and VIP chapters from subscription websites : http://www.qidian.com/ and http://www.zongheng.com/. After fetching new chapters, the app will store them into the Mongo database, then updates the database periodically. This app could be the backend for a pirate website because it can update novels for free for as many as you want.

The app has the following dependencies:

Mongodb (Install the database first)  
Gson  
Jsoup  
Logback  
