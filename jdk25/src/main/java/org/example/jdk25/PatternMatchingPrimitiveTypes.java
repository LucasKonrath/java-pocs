
void main(){
    Object obj = "Hello, World!";
    if(obj instanceof String s){
        System.out.println(s.toUpperCase());
    } else if (obj instanceof Long){
        System.out.println("It's a long: " + obj);
    } else {
        System.out.println("Not a string");
    }
}