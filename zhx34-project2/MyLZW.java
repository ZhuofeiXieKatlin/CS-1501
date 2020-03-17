public class MyLZW {
    private static final int Min = 9; 
    private static final int R = 256;        // number of input chars
    private static int W = 9;        // the width of code book is from 9 bits 
    private static int L = (int)Math.pow(2, W);     // number of codewords = 2^W
    

    private static final int MAXIMUM_LENGTH = 16; // the length of the maximum width of the code books 
    private static final int MAXIMUM_LENGTH_FOR_ARRAY = (int)Math.pow(2, MAXIMUM_LENGTH); 
    
    private static char Mode_type = 'n'; 

    private static int UnCompressed_data = 0; 
    private static int Compressed_data = 0; 
    private static double Original_Compression_rate = 0.0; 
    private static double new_ration = 0.0; 
    private static final double Compression_ratio = 1.1; // the compression ratio used to determine whether reset or not

    // when ratio > 1.1 should reset 

    public static void write_mode(char mode){
        BinaryStdOut.write(mode); 
    }

    public static boolean calculate_ratio(int uncompress, int compress, double ratio){
        new_ration = (double)uncompress/(double)compress; 
        double new_original_ratio = ratio/new_ration; 
        if(new_original_ratio>Compression_ratio){
            return true; 
        }
        return false; 
    }

    public static void initialRatiosize(){
        UnCompressed_data = 0; 
        Compressed_data = 0; 
        Original_Compression_rate = 0.0;
        new_ration = 0.0; 
    }
    public static void compress() { 
        String input = BinaryStdIn.readString(); 
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);
        int code = R+1;  // R is codeword for EOF


        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);  // Find max prefix match s.
            BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
            int t = s.length();
            if (t < input.length() && code < L)    // Add s to symbol table.
                st.put(input.substring(0, t + 1), code++);
            else if(t<input.length() && code>=L){ 
                if(W<MAXIMUM_LENGTH){
                    W = W + 1; 
                    L = (int) Math.pow(2, W); 
                    st.put(input.substring(0, t + 1), code++);
                }
            }
            input = input.substring(t);
        }
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    } 

    public static void reset_mode(){
        String input = BinaryStdIn.readString(); 
        TST<Integer> st = new TST<Integer>();
        for(int i=0; i< R; i++){
            st.put("" + (char) i,i); 
        }
        int code = R+1; 

        while(input.length()>0){
            String s = st.longestPrefixOf(input); 
            BinaryStdOut.write(st.get(s),W);
            int t = s.length(); 
            if(t<input.length() && code < L){
                st.put(input.substring(0, t + 1), code++);
            }else if(t<input.length() && code>=L){
                if(W<MAXIMUM_LENGTH){
                    W = W +1; 
                    L = (int) Math.pow(2, W); 
                    st.put(input.substring(0, t + 1), code++);
                }else if(W == MAXIMUM_LENGTH){
                    st = new TST<Integer>(); 
                    for(int i=0; i< R; i++){
                        st.put("" + (char) i,i); 
                    }
                    W = Min; 
                    L = (int) Math.pow(2,W); 
                    code = R+1; 
                    st.put(input.substring(0, t + 1), code++);
                }
            }
            input = input.substring(t);
        }
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    }

    public static void monitor_mode(){
        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);
        int code = R+1;  // R is codeword for EOF

        initialRatiosize();

        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);  // Find max prefix match s.
            BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
            int t = s.length();
            UnCompressed_data += (t*8); 
            Compressed_data += W;  
            //System.out.println(Compressed_data);
            if (t < input.length() && code < L){ // Add s to symbol table.
                st.put(input.substring(0, t + 1), code++);
                Original_Compression_rate = (double)UnCompressed_data/(double)Compressed_data; 
            }    
            else if(t<input.length() && code>=L){ 
                if(W<MAXIMUM_LENGTH){
                    W = W+1; 
                    L = (int) Math.pow(2, W); 
                    st.put(input.substring(0, t + 1), code++);
                }else if(W == MAXIMUM_LENGTH){
                    if(calculate_ratio(UnCompressed_data,Compressed_data,Original_Compression_rate)){
                        st = new TST<Integer>(); 
                        for(int i=0; i< R; i++){
                            st.put("" + (char) i,i); 
                        }
                        W = Min; 
                        L = (int) Math.pow(2,W); 
                        code = R+1; 
                        st.put(input.substring(0, t + 1), code++);
                    }
                }
            }
            input = input.substring(t);          
        }
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    }


    public static void expand() {
        String[] st = new String[MAXIMUM_LENGTH_FOR_ARRAY];
        int i;

        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                      

        int codeword = BinaryStdIn.readInt(W);
        if (codeword == R) return;           // expanded message is empty string
        String val = st[codeword];

        while (true) {
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(W);
            if (codeword == R) break;
            String s = st[codeword];
            if (i == codeword) s = val + val.charAt(0);   // special case hack
            if (i < L){
                st[i++] = val + s.charAt(0); 
            } 
            val = s;
            if(i>=L){
                if(W < MAXIMUM_LENGTH){
                    W= W+1; 
                    L = (int)Math.pow(2, W); 
                }
            }
        }
        BinaryStdOut.close();
    }

    public static void expand_reset_mode(){
        String[] st = new String[MAXIMUM_LENGTH_FOR_ARRAY];
        int i;

        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                      

        int codeword = BinaryStdIn.readInt(W);
        //System.err.println(codeword);
        if (codeword == R) return;           // expanded message is empty string
        String val = st[codeword];
        //System.err.println(val);

        while (true) {
            
            BinaryStdOut.write(val); 
            codeword = BinaryStdIn.readInt(W);
            if (codeword == R) break;
            String s = st[codeword];
            if (i == codeword) s = val + val.charAt(0);   // special case hack
            if (i < L){
                st[i++] = val + s.charAt(0); 
            } 
            val = s;

            if(i>=L){
                if(W < MAXIMUM_LENGTH){
                    W= W+1; 
                    L = (int)Math.pow(2, W); 
                }
                else if(W==MAXIMUM_LENGTH){
                    st = new String[MAXIMUM_LENGTH_FOR_ARRAY];
                    for (i = 0; i < R; i++)
                        st[i] = "" + (char) i;
                    st[i++] = ""; 
                    W = Min; 
                    L = (int)Math.pow(2, Min); 
                }
            }
        }
        BinaryStdOut.close(); 
    }

    public static void expand_monitor_mode(){
        String[] st = new String[MAXIMUM_LENGTH_FOR_ARRAY];
        int i;

        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                         

        int codeword = BinaryStdIn.readInt(W);
        if (codeword == R) return;          
        String val = st[codeword];
        //int t = val.length();

        initialRatiosize(); 

        while (true) {
            UnCompressed_data += (val.length() * 8); 
            Compressed_data += W; 
            BinaryStdOut.write(val);
            
            if(i>=L){
                if(W < MAXIMUM_LENGTH){
                    W= W+1; 
                    L = (int)Math.pow(2, W); 
                    Original_Compression_rate = (double)UnCompressed_data/(double) Compressed_data;
                }
                if(W == MAXIMUM_LENGTH){
                    if(calculate_ratio(UnCompressed_data,Compressed_data,Original_Compression_rate)){
                        //System.err.println("here"); 
                        st = new String[MAXIMUM_LENGTH_FOR_ARRAY];
                        for (i = 0; i < R; i++)
                            st[i] = "" + (char) i;
                        st[i++] = ""; 
                        W = Min; 
                        L = (int)Math.pow(2, Min); 
                        Original_Compression_rate=0.0;
                    }
                }
            }
            codeword = BinaryStdIn.readInt(W);
            if (codeword == R) break;
            String s = st[codeword];
            if (i == codeword) s = val + val.charAt(0);   // special case hack
            if (i < L){
                st[i++] = val + s.charAt(0); 
                Original_Compression_rate = (double)UnCompressed_data/(double) Compressed_data;
                //System.err.println(Original_Compression_rate);
            } 
            val = s;
        }
        BinaryStdOut.close();
    }
    public static void main(String[] args) {

        if(args.length>=2){
            Mode_type = args[1].charAt(0); 
            write_mode(Mode_type); 
            if(args[1].equals("n")){
                compress();
            }else if(args[1].equals("r")){
                reset_mode(); 
            }else if(args[1].equals("m")){
                monitor_mode(); 
            } 
        }
        else if(args.length == 1){
            if(args[0].equals("-")){
                write_mode(Mode_type);
                compress();
            }
            else if(args[0].equals("+")){
                Mode_type = BinaryStdIn.readChar(); 
                if(Mode_type == 'n'){
                    //System.err.println(Mode_type);
                    expand();
                }else if(Mode_type == 'r'){
                    //System.err.println(Mode_type);
                    expand_reset_mode(); 
                }else if(Mode_type == 'm'){
                    //System.err.println(Mode_type);
                    expand_monitor_mode();
                }
            }
        } 

        else throw new IllegalArgumentException("Illegal command line argument");
    }

}
