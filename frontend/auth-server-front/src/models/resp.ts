export interface Resp<T> {
  /** message being returned */
  msg: string;

  /** whether current response has an error */
  hasError: boolean;

  /** data */
  data: T;
}

export function mockRespOf(data: any): Resp<any>{
  return {
    msg: '',
    hasError: false,
    data: data,
  }
}
