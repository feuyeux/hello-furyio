import Fury from '@furyjs/fury';

/**
 * @furyjs/hps use v8's fast-calls-api that can be called directly by jit, ensure that the version of Node is 20 or above.
 * Experimental feature, installation success cannot be guaranteed at this moment
 * If you are unable to install the module, replace it with `const hps = null;`
 **/
import hps from '@furyjs/hps';

const fury = new Fury({ hps });
const input = fury.serialize('hello fury');
const result = fury.deserialize(input);
console.log(result);