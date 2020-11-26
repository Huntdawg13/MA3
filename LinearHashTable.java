/*
 *  Microassignment: Probing Hash Table addElement and removeElement
 *
 *  LinearHashTable: Yet another Hash Table Implementation
 * 
 *  Contributors:
 *    Bolong Zeng <bzeng@wsu.edu>, 2018
 *    Aaron S. Crandall <acrandal@wsu.edu>, 2019
 * 
 *  Copyright:
 *   For academic use only under the Creative Commons
 *   Attribution-NonCommercial-NoDerivatives 4.0 International License
 *   http://creativecommons.org/licenses/by-nc-nd/4.0
 *
 *   Student Editing: Hunter McClure
 *   CptS 233: MicroAssignment #3
 *   Date: 11/25/2020
 *   gitRepo url: https://github.com/Huntdawg13/MA3.git
 */


class LinearHashTable<K, V> extends HashTableBase<K, V>
{
	// Linear and Quadratic probing should rehash at a load factor of 0.5 or higher
    private static final double REHASH_LOAD_FACTOR = 0.5;

    // Constructors
    public LinearHashTable()
    {
        super();
    }

    public LinearHashTable(HasherBase<K> hasher)
    {
        super(hasher);
    }

    public LinearHashTable(HasherBase<K> hasher, int number_of_elements)
    {
        super(hasher, number_of_elements);
    }

    // Copy constructor
    public LinearHashTable(LinearHashTable<K, V> other)
    {
        super(other);
	}
    
   
    // ***** MA Section Start ************************************************ //

    // Concrete implementation for parent's addElement method
    public void addElement(K key, V value)
    {
        // Check for size restrictions
        resizeCheck();
 
        // Calculate hash based on key
        int hash = super.getHash(key);

        // MA TODO: find empty slot to insert (update HashItem as necessary)
         HashItem<K, V> hash_position = _items.elementAt(hash);
         HashItem<K, V> item = new HashItem<K, V>(key, value, false);
         
         //search for open slot
         while(hash_position.isEmpty() == false)
         {
            hash = (hash + 1) % _items.size();
            hash_position = _items.elementAt(hash);
         }
         
         //add element into open/empty slot
         _items.setElementAt(item, hash);
         
         //increase size of elements
         _number_of_elements++;         
    }

    // Removes supplied key from hash table
    public void removeElement(K key)
    {
        // Calculate hash from key
        int hash = super.getHash(key);

        // MA TODO: find slot to remove. Remember to check for infinite loop!
        //  ALSO: Use lazy deletion - see structure of HashItem
        HashItem<K, V> position = _items.elementAt(hash);
        int hashSize = _items.size();
        
        //find key in hash
        if(containsElement(key) == true)
        {
            for(int i = 0; i < hashSize - 1; i++)
            {
                if(!position.isEmpty()) 
                {
                    //if found then leave loop and lazy delete
                    if(position.getKey().equals(key)) 
                    {
                        _number_of_elements--; //decrease size
                        _items.elementAt(hash).setIsEmpty(true);
                        break;
                    }
                }
               hash = (hash + 1) % hashSize;
               position = _items.elementAt(hash);
            }
        
        }     
         
      resizeCheck(); //check if hash needs resize    
            
    }
    
    // ***** MA Section End ************************************************ //
    

    // Public API to get current number of elements in Hash Table
	public int size() {
		return this._number_of_elements;
	}

    // Public API to test whether the Hash Table is empty (N == 0)
	public boolean isEmpty() {
		return this._number_of_elements == 0;
	}
    
    // Returns true if the key is contained in the hash table
    public boolean containsElement(K key)
    {
        int hash = super.getHash(key);
        int hashSize = _items.size();
        HashItem<K, V> position = _items.elementAt(0);
        for(int i = 0; i < hashSize - 1; i++)
        {
            if(!position.isEmpty()) 
            {
                if(position.equals(_items.elementAt(hash))) 
                    return true;
            }
            hash = (hash + 1) % hashSize;
            position = _items.elementAt(hash);
        }
        
        return false;
    }
    
    // Returns the item pointed to by key
    public V getElement(K key)
    {
        int hash = super.getHash(key);
        HashItem<K, V> slot = _items.elementAt(hash);
        
        // Left incomplete to avoid hints in the MA :)
        return null;
    }

    // Determines whether or not we need to resize
    //  to turn off resize, just always return false
    protected boolean needsResize()
    {
        // Linear probing seems to get worse after a load factor of about 50%
        if (_number_of_elements > (REHASH_LOAD_FACTOR * _primes[_local_prime_index]))
        {
            return true;
        }
        return false;
    }
    
    // Called to do a resize as needed
    protected void resizeCheck()
    {
        // Right now, resize when load factor > 0.5; it might be worth it to experiment with 
        //  this value for different kinds of hashtables
        if (needsResize())
        {
            _local_prime_index++;

            HasherBase<K> hasher = _hasher;
            LinearHashTable<K, V> new_hash = new LinearHashTable<K, V>(hasher, _primes[_local_prime_index]);

            for (HashItem<K, V> item: _items)
            {
                if (item.isEmpty() == false)
                {
                    // Add element to new hash table
                    new_hash.addElement(item.getKey(), item.getValue());
                }
            }

            // Steal temp hash object's internal vector for ourselves
            _items = new_hash._items;
        }
    }

    // Debugging tool to print out the entire contents of the hash table
	public void printOut() {
		System.out.println(" Dumping hash with " + _number_of_elements + " items in " + _items.size() + " buckets");
		System.out.println("[X] Key	| Value	| Deleted");
		for( int i = 0; i < _items.size(); i++ ) {
			HashItem<K, V> curr_slot = _items.get(i);
			System.out.print("[" + i + "] ");
			System.out.println(curr_slot.getKey() + " | " + curr_slot.getValue() + " | " + curr_slot.isEmpty());
		}
	}
}