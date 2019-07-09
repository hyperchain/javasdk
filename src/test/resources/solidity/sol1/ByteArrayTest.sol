contract ByteArrayTest {
    bytes name;

    function ByteArrayTest(bytes name1) public {
        name = name1;
    }

    function testArray(uint32[2] a, bool[2] b) public returns (uint32[2], bool[2]){
        return (a, b);
    }
}