package com.example.life4paws;

public class DogBreedIdx
{
	// Might sure make that this list is in sync with the website's list
	public static final String	dog_breeds[]	= { "Affenpinscher ", "Afghan Hound", "Airedale Terrier", "Akbash", "Akita", "Alaskan Malamute", "American Bulldog",
			"American Eskimo Dog", "American Staffordshire Terrier", "Anatolian Shepherd", "Australian Cattle Dog/Blue Heeler", "Australian Kelpie",
			"Australian Shepherd", "Australian Terrier", "Basenji", "Basset Hound", "Beagle", "Bearded Collie", "Beauceron", "Belgian Shepherd Dog Sheepdog",
			"Belgian Shepherd Laekenois", "Belgian Shepherd Malinois", "Belgian Shepherd Tervuren", "Bernese Mountain Dog", "Bichon Frise",
			"Black and Tan Coonhound", "Black Labrador Retriever", "Black Mouth Cur", "Bloodhound", "Bluetick Coonhound", "Border Collie", "Border Terrier",
			"Borzoi", "Boston Terrier", "Bouvier des Flanders", "Boxer", "Boykin Spaniel", "Briard", "Brittany Spaniel", "Brussels Griffon", "Bull Terrier",
			"Bullmastiff", "Cairn Terrier", "Canaan Dog", "Cane Corso Mastiff", "Carolina Dog", "Catahoula Leopard Dog", "Cattle Dog",
			"Cavalier King Charles Spaniel", "Chesapeake Bay Retriever", "Chihuahua ", "Chihuahua Long Haired", "Chihuahua under 8 lbs", "Chinese Crested Dog",
			"Chinese Foo Dog", "Chocolate Labrador Retriever", "Chow Chow", "Clumber Spaniel", "Cockapoo", "Cocker Spaniel", "Collie", "Coonhound", "Corgi",
			"Coton de Tulear", "Dachshund - long haired", "Dachshund - smoth", "Dachshund - wired", "Dalmatian", "Dandi Dinmont Terrier", "Doberman Pinscher",
			"Dogo Argentino", "Dogue de Bordeaux", "Dutch Shepherd", "English Bulldog", "English Cocker Spaniel", "English Foxhound", "English Pointer",
			"English Setter", "English Shepherd", "English Springer Spaniel", "English Toy Spaniel", "Entlebucher", "Eskimo Dog", "Field Spaniel",
			"Fila Brasileiro", "Finnish Lapphund", "Finnish Spitz", "Flat-coated Retriever", "Fox Terrier - smooth", "Fox Terrier - wired", "Foxhound",
			"French Bulldog", "German Pinscher", "German Shepherd Dog", "German Shorthaired Pointer", "German Wirehaired Pointer", "Glen of Imaal Terrier",
			"Golden Retriever", "Gordon Setter", "Grand Basset Griffon Vendeen Mix", "Great Dane", "Great Pyrenees", "Greater Swiss Mountain Dog", "Greyhound",
			"Havanese", "Hound", "Hovawart", "Husky", "Ibizan Hound", "Illyrian Sheepdog", "Irish Setter", "Irish Terrier", "Irish Wolfhound",
			"Italian Greyhound", "Italian Spinone", "Jack Russell Terrier", "Jack Russell Terrier (Parson Russell Terrier)", "Japanese Chin", "Jindo",
			"Kai Dog", "Karelian Bear Dog", "Keeshond", "Kerry Blue Terrier", "King Charles Spaniel", "Kishu", "Komondor", "Kuvasz", "Kyi Leo", "L?wchen",
			"Labrador Retriever", "Lancashire Heeler", "Leonberger", "Lhasa Apso", "Maltese", "Manchester Terrier", "Maremma Sheepdog", "Mastiff", "McNab",
			"Miniature Pinscher", "Mix Unknown", "Mountain Cur", "Mountain Dog", "Neapolitan Mastiff", "New Guinea Singing Dog", "Newfoundland Dog",
			"Norfolk Terrier", "Norwegian Elkhound", "Norwich Terrier", "Nova Scotia Duck-Tolling Retriever", "Old English Sheepdog", "Otterhound", "Papillon",
			"Patterdale Terrier (Fell Terrier)", "Pekingese", "Petit Basset Griffon Vendeen", "Pharaoh Hound", "Pit Bull Terrier", "Plott Hound",
			"Podengo Portugueso", "Pointer", "Polish Lowland Sheepdog", "Pomeranian", "Poodle - Mini", "Poodle - Standard", "Poodle - Toy",
			"Portuguese Water Dog", "Presa Canario", "Pug", "Puggle", "Puli", "Pumi", "Queensland Heeler", "Rat Terrier", "Redbone Coonhound", "Retriever",
			"Rhodesian Ridgeback", "Rottweiler", "Saint Bernard St. Bernard", "Saluki", "Samoyed", "Schipperke", "Schnauzer", "Schnauzer - mini",
			"Scottish Deerhound", "Scottish Terrier Scottie", "Sealyham Terrier", "Setter", "Shar Pei", "Sheep Dog", "Sheltie", "Shepherd",
			"Shetland Sheepdog Sheltie", "Shiba Inu", "Shih Tzu", "Siberian Husky", "Silky Terrier", "Skye Terrier", "Sloughi", "Smooth Fox Terrier",
			"Spaniel", "Spitz", "Staffordshire Bull Terrier", "Sussex Spaniel", "Swedish Vallhund", "Terrier", "Thai Ridgeback", "Tibetan Mastiff",
			"Tibetan Spaniel", "Tibetan Terrier", "Tosa Inu", "Toy Fox Terrier", "Treeing Walker Coonhound", "Unknown", "Vizsla", "Weimaraner", "Welsh Corgi",
			"Welsh Springer Spaniel", "Welsh Terrier", "West Highland White Terrier Westie", "Wheaten Terrier", "Whippet", "White German Shepherd",
			"Wire-haired Pointing Griffon", "Wirehaired Terrier", "Xoloitzcuintle/Mexican Hairless", "Yellow Labrador Retriever", "Yorkshire Terrier Yorkie" };
	
	private static final int base_breedid = 59177; // This one is "Affenpinscher" 
	
	static String getBreedName(int breed_id)
	{
		String breed = null;
		int idx = breed_id - base_breedid;
		breed = dog_breeds[idx];
		return breed;
	}
	
	/**
	 * Maps a breed name as given on the ishelters website to the id as given in the drop down box
	 * Potential problem with data not being in sync with the website since the above array is generated based on the greasemonkey script
	 * @return breed id as listed out on the animal add.php
	 */
	static int getBreedID(String breed_name)
	{
		for(int i =0; i < dog_breeds.length; ++i)
		{
			String cur = dog_breeds[i];
			if(breed_name.equalsIgnoreCase(cur))
				return base_breedid + i;
		}
		return -1;
	}
}
