use fvm_std::runtime;

/// 这是一个setHash合约，拥有两个合约方法
///  - set_hash: 传入32字节的key和32字节的value
///  - get_hash: 根据32字节的key查询返回32字节的value

#[no_mangle]
fn deploy() {}

#[no_mangle]
fn invoke() {
    let set_hash_signature: [u8; 4] = [215, 250, 16, 07];
    let get_hash_signature: [u8; 4] = [60, 245, 04, 10];

    let input = runtime::input();
    let input_data = input.as_slice();

    //参数检查
    if input_data.len() < 36 {
        runtime::revert("param not enough");
    }
    let function_selector = &input_data[0..4];
    let key = &input_data[4..36];

    if function_selector == set_hash_signature {
        let value = &input_data[36..];
        set_hash(&key, &value);
    }
    if function_selector == get_hash_signature {
        runtime::ret(get_hash(&key).as_slice());
    }
    runtime::revert("no such method to call")
}

fn set_hash(key: &[u8], value: &[u8]) {
    runtime::storage_write(key, "".as_bytes(), value);
}

fn get_hash(key: &[u8]) -> Vec<u8> {
    runtime::storage_read(key, "".as_bytes()).unwrap()
}

#[cfg(test)]
mod tests {
    use crate::invoke;
    use fvm_mock::build_runtime;

    #[test]
    fn test_set_get() {
        let insert_data = || {};

        let set_hash_signature: [u8; 4] = [215, 250, 16, 07];
        let get_hash_signature: [u8; 4] = [60, 245, 04, 10];
        let mut key = [0; 32];
        let mut value = [0; 32];
        let kim = "Kim".as_bytes();
        for i in 0..kim.len() {
            key[i] = *kim.get(i).unwrap()
        }
        let great = "great".as_bytes();
        for i in 0..great.len() {
            value[i] = *great.get(i).unwrap()
        }

        let handle = build_runtime();

        // call set hash
        let mut set_hash_input = set_hash_signature.to_vec();
        set_hash_input.append(&mut key.to_vec());
        set_hash_input.append(&mut value.to_vec());
        handle.input(set_hash_input.as_slice());
        invoke();

        // call get hash
        let mut get_hash_input = get_hash_signature.to_vec();
        get_hash_input.append(&mut key.to_vec());
        handle.input(get_hash_input.as_slice());
        invoke();

        let result = handle.get_ret();
        assert_eq!(
            result,
            vec![
                103, 114, 101, 97, 116, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0
            ]
        );
    }
}
