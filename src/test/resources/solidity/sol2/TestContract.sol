contract TypeTestContract {
    bytes name;

    event eventA(bytes a, string indexed b);
    event eventB();
    event eventB(string a);

    function TypeTestContract(bytes name1) {
        name = name1;
    }

    function TestBytes32Array(bytes32[2] a) returns (bytes32[2]){
        return a;
    }

    function TestBytes(bytes a) returns (bytes) {
        return a;
    }

    function TestBytes32(bytes32 a) returns (bytes32) {
        eventA(name, "hello");
        eventB();
        return a;
    }

    function TestBytes1(bytes1 a) returns (bytes1) {
        return a;
    }

    function TestInt(int a) returns (int) {
        return a;
    }

    function TestUint(uint8 a) returns (uint8) {
        return a;
    }

    function TestBool(bool a) returns (bool) {
        return a;
    }

    function TestAddress(address a) returns (address) {
        return a;
    }

    function TestString(string a) returns (string) {
        return a;
    }
    // array
    function TestIntArray(int[3] a) returns (int[3]) {
        return a;
    }

    function TestInt8Array(int8[3] a) returns (int8[3]) {
        return a;
    }

    function TestUintArray(uint[3] a) returns (uint[3]) {
        return a;
    }

    function TestUint8Array(uint8[3] a) returns (uint8[3]) {
        return a;
    }

    function TestBoolArray(bool[3] a) returns (bool[3]) {
        return a;
    }

    function TestAddressArray(address[2] a) returns (address[2]) {
        return a;
    }

    function getName() returns (bytes) {
        return name;
    }

    function getName(bytes32 a) returns (bytes32) {
        return a;
    }

    // 为新特性，暂不支持
    //function TestStringArray(string[3] a) returns (string[3]) {
    //    return a;
    //}
    //function TestBytesArray(bytes[3] a) returns (bytes[3]) {
    //    return a;
    //}
}