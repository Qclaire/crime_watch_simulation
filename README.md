# crime_watch_simulation
### Heard of bloom filters? 

A Bloom filter is a very interesting data structure. It allows for super fast and lightweight check of the existence of information without retrieving the information itself. 
I mean a bloom filter can help you answer the question of whether an entry exists or not but because it does not store the data itself but a hash of the data, It does not allow retrieval of the information itself. 

#### Who Uses it? 
Ever wondered who also thinks a bloom filter is interesting? Well, Medium, the blogging company! They use it to predict if a user has already seen an article to avoid recommending articles you've already read.
So now you know why you see a different article everyday on your medium feed.

#### But I still get Articles I have read already, How's that?

Well, nothing is perfect! This has to do with the way bloom filters work. They hash the ipnut, and and determines and index in an arrray tfor the data. but instead of storing the data or the hash, they rather change the value at that index to 1 or true depending on what data structure  is used. By this you must have deduced that the data structure must be a bit array, an array that can store only 0 and 1 or true and false, or high and low or any other variations.