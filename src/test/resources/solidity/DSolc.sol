contract DSolc {
    struct Bill{
         bytes32 no;
        int amt;
    }

    mapping (string => mapping(string => Bill)) billMap;
    // Bill[][] billss;

    function putMap(string memory key1, string memory key2, int amt) public {
        Bill memory bill1 = Bill(amt);
        Bill memory bill2 = Bill(amt);
        billMap[key1][key2] = bill1;
        billMap[key2][key1] = bill2;
        billMap[key1][key1] = bill1;
    }

    function getMap(string memory key1, string memory key2) public returns(int) {
         mapping(string => Bill) bills = billMap[key1];
        Bill storage bill = billMap[key1][key2];

        Bill storage bill2 = bills[key2];

        return bill.amt + bill2.amt;
    }
}