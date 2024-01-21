//Team Member Details
//Konark shah - 40232277
//Het Jatin Dalal - 40200513

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.io.*;

public class ElasticERL {

    //taking a set of equipment names for randomly taking value for the key to be inserted
    public static final String[] equipmentNames = {"ECG Machine", "Ultrasound Machine", "X-Ray Machine", "Ventilator",
            "Defibrillator","Anesthesia Machine", "Patient Monitor", "Blood Gas Analyzer", "Infusion Pump", "Autoclave",
            "Surgical Table", "Medical Refrigerator", "Endoscope", "Dialysis Machine", "Oxygen Concentrator",
            "Nebulizer", "Spirometer", "Glucometer", "Blood Pressure Monitor", "Stethoscope",
            "Fetal Doppler", "EKG Machine", "Pulse Oximeter", "Tourniquet", "Tympanometer",
            "Blood Warmer", "Catheter", "Feeding Pump", "Enteral Feeding Pump", "Defecography Chair"};

    //taking a set of equipment brand for randomly taking value for the key to be inserted
    public static final String[] equipmentBrands = {"GE Healthcare", "Siemens Healthineers", "Philips Healthcare", "Medtronic", "Stryker",
            "Baxter International", "Fresenius Medical Care", "3M Healthcare", "Johnson & Johnson",
            "Hill-Rom", "Smith & Nephew", "Zimmer Biomet", "Boston Scientific", "Abbott Laboratories",
            "Becton, Dickinson and Company", "Cerner Corporation", "Roche Diagnostics", "PerkinElmer",
            "Alcon", "Allergan", "Bausch + Lomb", "Medline Industries", "Teleflex", "Halyard Health",
            "BD Bard", "Karl Storz", "Olympus", "Fujifilm", "Varian Medical Systems", "Elekta"
    };

    //taking a set of equipment models for randomly taking value for the key to be inserted
    public static final String[] equipmentModels = {"MAC 5500 HD ECG System", "LOGIQ E10 Ultrasound System", "Precision 500D Digital Radiography System",
            "Avea Ventilator", "Zoll X Series Defibrillator", "Aespire 7900 Anesthesia Machine",
            "Philips IntelliVue MX750 Patient Monitor", "ABL800 FLEX Blood Gas Analyzer",
            "Baxter Sigma Spectrum Infusion Pump", "Tuttnauer EZ9 Autoclave", "Hill-Rom 1000 Surgical Table",
            "Thermo Scientific TSX Series Medical Refrigerator", "Karl Storz IMAGE1 SPIES Endoscope",
            "Fresenius Medical Care 5008 Dialysis Machine", "Inogen One G3 Portable Oxygen Concentrator",
            "Pari Trek S Compact Nebulizer", "MIR Spirodoc Spirometer", "Contour Next One Glucometer",
            "Welch Allyn Connex ProBP 3400 Digital Blood Pressure Monitor", "Littmann Classic III Stethoscope",
            "Bistos FetalCare Fetal Doppler", "Burdick Vision Premier EKG Machine",
            "Nonin Onyx Vantage 9590 Pulse Oximeter", "Stryker Tourniquet System", "Amplivox 240 Portable Audiometer",
            "Baxter Flo-Gard 6200 Feeding Pump", "CORPAK Enteral Feeding Pump", "Biodex Ultra Pro Defecography Chair"};


    AVLTree bigADT = new AVLTree();
    ArrayList smallADT = new ArrayList();
    int currentSize = 0;
    boolean transferToBigADT = true;
    int threshold;

    public ElasticERL()
    {

    }
    // generates a random 8 digit key
    public long generate()
    {
        Random random = new Random();
        long randomNum = random.nextInt(90000000) + 10000000;
        return randomNum;
    }

    //generates a random value from the set of values list provided
    public String generateValue()
    {
        Random rand = new Random();
        int randomNum = rand.nextInt(27) + 1;

        String equipmentName = equipmentNames[randomNum];
        String equipmentBrand = equipmentBrands[randomNum];
        String equipmentModel = equipmentModels[randomNum];

        return "{ Equipment Name: "+equipmentName+", Equipment Brand: "+equipmentBrand+", Equipment Model: "+equipmentModel+" }";
    }

    //adding the key and value from one of bigADT and smallADT according to the threshold
    public void add(long key,String value)
    {
        if(currentSize<=threshold)
        {

            smallADT.add(key,value);
        }
        else
        {
            if(currentSize==threshold+1 && transferToBigADT){
                grow();
                transferToBigADT = false;
            }

            bigADT.root = bigADT.insert(key,value,bigADT.root);
        }
        currentSize++;
    }
    //remove the key and value from one of bigADT and smallADT according to the threshold
    public void remove(long key,String value)
    {
        if(currentSize<threshold)
        {
            if(currentSize==threshold && !transferToBigADT)
            {
                shrink();
                transferToBigADT = true;
            }
            smallADT.remove(key);
        }
        else
        {
            if(currentSize==threshold){
                grow();
            }

            bigADT.root = bigADT.remove(bigADT.root,key);
        }
        currentSize--;
    }

    //returns all keys from using allKeys function from one of bigADT and smallADT according to the threshold
    public void allKeys(){
        if(currentSize<threshold)
        {
            smallADT.allKeys();
        }
        else
        {
            bigADT.allKeys(bigADT.root);
        }
    }

    //returns the value of given key from using getValues function from one of bigADT and smallADT according to the threshold
    public void getValues(long key)
    {
        if(currentSize<threshold)
        {
            smallADT.getValues(key);
        }
        else
        {
            bigADT.getValues(key);
        }
    }

    //returns the key from using nextKey function from one of bigADT and smallADT according to the threshold
    public long nextKey(long key)
    {
        if(currentSize<threshold)
        {
            return smallADT.nextKey(key);
        }

        return bigADT.nextKey(key);

    }

    //returns the key from using prevKey function from one of bigADT and smallADT according to the threshold
    public long prevKey(long key)
    {
        if(currentSize<threshold)
        {
            return smallADT.prevKey(key);
        }

        return bigADT.prevKey(key);
    }

    //returns the count of keys in range from using rangeKey function from one of bigADT and smallADT according to the threshold
    public long rangeKey(long key1,long key2)
    {
        if(currentSize<threshold)
        {
            return smallADT.rangeKey(key1,key2);
        }

        return bigADT.rangeKey(bigADT.root,key1,key2);
    }

    //decides when to grow the ADT according to the threshold set and copies all the keys
    //from arrayList to AVL tree by inserting in the tree
    public void grow()
    {
        for(int i =0; i<smallADT.size;i++){
            bigADT.insert(smallADT.elasticArrayList[i].key,smallADT.elasticArrayList[i].value, bigADT.root);
            smallADT = new ArrayList();
        }
    }

    //decides when to shrink the ADT according to the threshold set and copies all the keys
    //from AVL tree using inorder traversal and copies them in arrayList
    public void shrink(){
        ArrayList list2 = new ArrayList();
        bigADT.copyToList(bigADT.root, list2);

        smallADT = list2;
        bigADT = new AVLTree();
    }

    public void setThreshold(int threshold)
    {
        this.threshold = threshold;
    }

    class AVLTree{
        Node root;
        public class Node
        {
            Node left;
            Node right;
            long key;
            int height;
            String value;

            Node(long key, String value){
                this.key =  key;
                this.value =  value;
                this.height =  1;
            }
        } // end of inner class

        //gives height of the node
        public int height(Node node){
            if(node == null){
                return 0;
            }
            return node.height;
        }

        //returns the balance factor of the node is the node is having delta height >1 or <-1 then we will do balancing
        public int deltaHeight(Node node){
            return height(node.left)-height(node.right);
        }

        //insertion of node according to property of AVL tree
        public Node insert(long key, String value, Node node) {
            if (node == null) {
                Node nn = new Node(key, value);
                return nn;
            }

            if (key < node.key) {
                node.left = insert(key, value, node.left);
            } else if (key > node.key) {
                node.right = insert(key, value, node.right);
            }

            node.height = 1 + Math.max(height(node.left), height(node.right));

            //checks for one of the case of rotation out of LL, RR, LR, RL rotation to balance the tree
            if(deltaHeight(node)>1 && key< node.left.key){
                return rotateRight(node);
            }

            if(deltaHeight(node)<-1 && key>node.right.key) {
                return rotateLeft(node);
            }

            if(deltaHeight(node)<-1 && key<node.right.key) {
                node.right = rotateRight(node.right);
                return rotateLeft(node);
            }
            if(deltaHeight(node)>1 && key>node.right.key) {
                node.left = rotateLeft(node.left);
                return rotateRight(node);
            }

            return node;
        }

        //node removal logic and AVL tree height balancing
        public Node remove(Node node,long key){
            if(node == null){
                return null;
            }
            if(key<node.key){
                node.left = remove(node.left, key);
            }
            else if (key>node.key) {
                node.right = remove(node.right, key);
            }
            else{
                //case of deleting leaf node
                if(node.left == null && node.right == null){
                    return null;
                }
                // case 2 of deleting node with 1 child
                if (node.right == null) {
                    return node.left;
                }
                else if (node.left == null) {
                    return node.right;
                }

                //case 3 of deleting node with 2 child
                else{
                    Node nextSuccessor = inorderSuccessor(node.right);
                    node.key = nextSuccessor.key;
                    node.right = remove(nextSuccessor,nextSuccessor.key);
                }
            }
            node.height = Math.max(height(node.left), height(node.right)) + 1;

            //checks for one of the case of rotation out of LL, RR, LR, RL rotation to balance the tree
            if(deltaHeight(node)>1 && key< node.left.key){
                return rotateRight(node);
            }

            if(deltaHeight(node)<-1 && key>node.right.key) {
                return rotateLeft(node);
            }

            if(deltaHeight(node)<-1 && key<node.right.key) {
                node.right = rotateRight(node.right);
                return rotateLeft(node);
            }
            if(deltaHeight(node)>1 && key>node.right.key) {
                node.left = rotateLeft(node.left);
                return rotateRight(node);
            }

            return node;
        }

        //gives the inorder successor of the node
        public Node inorderSuccessor(Node node){
            while(node.left!= null){
                node = node.left;
            }
            return node;
        }

        //rotates the node to right to balance the tree AVL property
        public Node rotateRight(Node c){
            Node b = c.left;
            Node T3 = b.right;

            b.right = c;
            c.left = T3;

            c.height = Math.max(height(c.left), height(c.right)) + 1;
            b.height = Math.max(height(b.left), height(b.right)) + 1;

            return b;
        }

        //rotates the node to left to balance the tree AVL property
        public Node rotateLeft(Node c){
            Node b = c.right;
            Node T2 = b.left;

            b.left = c;
            c.right = T2;

            c.height = Math.max(height(c.left), height(c.right)) + 1;
            b.height = Math.max(height(b.left), height(b.right)) + 1;

            return b;
        }
        //inorder traversal of AVL tree giving sorted sequence of nodes
        public void inorderTraversal(Node node){
            if(node==null){
                return;
            }
            inorderTraversal(node.left);
            System.out.print(node.key + " ");
            inorderTraversal(node.right);
        }

        //copies the given AVL to ArrayList when it is time to shrink
        public ArrayList copyToList(Node node, ArrayList list){

            if (node != null) {
                copyToList(node.left, list);
                list.add(node.key,node.value);
                copyToList(node.right, list);
            }
            return list;
        }

        //returns the inorder successor in the AVL tree for the given key
        public long nextKey( long key) {

            Node successor = null;

            Node node = root;

            while(node != null)
            {
                if(key >= node.key)
                {
                    node = node.right;
                }
                else
                {
                    successor = root;
                    node = node.left;
                }
            }

            if(successor==null)
            {
                return -1;
            }

            return successor.key;
        }

        //returns the inorder predecessor in the AVL tree for the given key
        public long prevKey(long key){

            Node predecessor = null;
            Node node = root;

            while(node != null)
            {
                if(key > node.key)
                {
                    predecessor = node;
                    node = node.right;
                }
                else
                {
                    node = node.left;
                }
            }

            if(predecessor==null)
            {
                return -1;
            }

            return predecessor.key;
        }

        //returns all the keys in the given AVL tree in sorted manner
        public void allKeys(Node node)
        {
            if (node != null)
            {

                allKeys(node.left);
                System.out.print(node.key + " ");
                allKeys(node.right);


            }
        }

        //returns the value of the given key in the AVL tree
        public void getValues(long key)
        {
            Node node = root;
            while(node != null)
            {
                if(node.key==key)
                {
                    System.out.println(node.value);
                    return;
                }
                if(node.key<key)
                {
                    node = node.right;
                }
                else
                {
                    node = node.left;
                }
            }
        }

        //gives the number of keys in the given range of key1 and key2 inclusive
        public long rangeKey(Node node, long key1, long key2)
        {
            if(node == null){
                return 0;
            }

            if(node.key>= key1 && node.key<= key2){
                return 1 + rangeKey(node.left, key1, key2) + rangeKey(node.right, key1, key2);
            }

            if(node.key<key1){
                return  rangeKey(node.right, key1, key2);
            }

            else {
                return  rangeKey(node.left, key1, key2);
            }
        }

    }
    class ArrayList
    {
        Node[] elasticArrayList;
        int arrayMaxSize=100;
        static int size=0;
        class Node
        {
            long key;
            String value;

            Node(long key,String value)
            {
                this.key=key;
                this.value=value;
            }
        }
        ArrayList()
        {
            elasticArrayList = new Node[arrayMaxSize];
        }


        //inserts the new key to the end of the array list and increases its size after insertion
        public void add(long key, String value)
        {
            if(arrayMaxSize>size)
            {
                Node newNode = new Node(key, value);
                elasticArrayList[size] = newNode;
                size++;
            }
            else {
                expand(elasticArrayList,key,value);
            }
        }

        //once the size of the reached its max size it expands and creates a bigger array of double size giving a
        //constant amortized cost of O(N)
        void expand(Node[] elasticArray,long key,String value)
        {
            arrayMaxSize=arrayMaxSize *2;
            Node temp[]=new Node[arrayMaxSize];
            for(int i=0;i<size;i++)
            {
                temp[i]=elasticArray[i];
            }
            elasticArray=temp;
            Node newNode = new Node(key, value);
            elasticArray[size] = newNode;
            size++;

        }

        //removes key from the given
        public boolean remove(long key)
        {
            int index=getIndex(key);
            if (index==-1)
            {
                return false;
            }
            else
            {
                for(int i=index;i<size-1;i++)
                {
                    elasticArrayList[i]=elasticArrayList[i+1];

                }
                size--;
                return true;
            }
        }

        //returns the string storing the value for that particular key
        public String getValues(long key)
        {
            int index=getIndex(key);
            if (index==-1)
            {
                return "Entered key not found";
            }
            else
            {
                return elasticArrayList[index].value;
            }

        }

        //gives the key stored next to given key in the array list
        public long nextKey(long key)
        {
            int index=getIndex(key);
            if (index==-1)
            {
                return 0;
            }
            else
            {
                if(index+1!=size)
                    return elasticArrayList[index+1].key;
                else
                    return 0;
            }

        }

        //gives the key stored previous to given key in the array list
        public long prevKey(long key)
        {
            int index=getIndex(key);
            if (index==-1)
            {
                return 0;
            }
            else
            {
                if(index!=0)
                    return elasticArrayList[index-1].key;
                else
                    return 0;
            }

        }

        //gives the number of keys found in the range inclusive of key 1 and 2
        public int rangeKey(long key1,long key2)
        {
            int index1=getIndex(key1);
            int index2=getIndex(key2);
            if(index1 != -1 && index2 !=-1)
            {
                int number=Math.abs(index1-index2)+1;
                return number;
            }
            return 0;
        }

        //gives all the keys stored in the arrayList in a sorted manner
        public long[] allKeys()
        {
            long temp[] = new long[size];
            for(int i=0;i<size;i++)
            {
                temp[i]=elasticArrayList[i].key;
            }
            radixSort(temp,size);
            return temp;
        }

        //takes each element of the key and calls the count sort for sorting according to it
        public void radixSort(long[] temp,int size)
        {
            for(int pos=1,j=1;j<9;pos=pos*10,j++)
            {
                countSort(temp,size,pos);
            }

        }

        //sorts the keys on the basis of the given position of key
        public void countSort(long[] temp,int size,int pos)
        {
            long output[] = new long[size];
            int i;
            int count[] = new int[10];
            Arrays.fill(count, 0);

            for (i = 0; i < size; i++) {
                int x=(int)temp[i] / pos;
                count[x % 10]++;
            }

            for (i = 1; i < 10; i++)
                count[i] += count[i - 1];

            for (i = size - 1; i >= 0; i--)
            {
                int x=(int)temp[i] / pos;
                output[count[x % 10] - 1] = temp[i];
                count[x % 10]--;
            }

            for (i = 0; i < size; i++)
                temp[i] = output[i];
        }

        //searches the index of the given key in the array list
        public int getIndex(long key)
        {
            for(int i=0;i<size;i++)
            {
                if(elasticArrayList[i].key==key)
                {
                    return i;
                }
            }
            return -1;
        }

        //displays the elements of the array list
        void show()
        {
            for(int i=0;i<size;i++)
            {
                System.out.print(elasticArrayList[i].key+" ");
            }
        }
    }


    public static void main(String args[]){
        ElasticERL e1 = new ElasticERL();
        //setting a threshold of 2500 for shrinking and growing of the Elastic ERL
        e1.setThreshold(2500);

        try
        {
            Scanner scan1 = new Scanner(new FileInputStream("/Users/konarkshah/Downloads/EHITS_test_files/EHITS_test_file1.txt"));
            while(scan1.hasNextLine())
            {
                long inputKey = Long.parseLong(scan1.nextLine());
                e1.add(inputKey,e1.generateValue());
            }
        }
        catch(Exception err)
        {
            System.out.println(err.getMessage());
        }
        System.out.println(e1.generateValue());
        System.out.println(e1.generate());
    }
}



